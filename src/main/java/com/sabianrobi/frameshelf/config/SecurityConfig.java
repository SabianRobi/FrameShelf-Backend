package com.sabianrobi.frameshelf.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sabianrobi.frameshelf.entity.response.GetLoginUrlResponse;
import com.sabianrobi.frameshelf.entity.response.UserResponse;
import com.sabianrobi.frameshelf.security.CustomOAuth2User;
import com.sabianrobi.frameshelf.service.GoogleUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.*;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private GoogleUserService googleUserService;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://127.0.0.1:3000", "http://frameshelf.local:3000"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new StatelessAuthorizationRequestRepository();
    }

    @Bean
    SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .headers(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/login/oauth2/authorize/**",
                                "/api/v1/auth/login/oauth2/callback/**",
                                "/api/v1/user/me",
                                "/api/v1/auth/logout",
                                "/error"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                );

        // Configure OAuth2 login
        http.oauth2Login(oauth2 -> {
            // Configure the user info endpoint to use our custom service
            oauth2.userInfoEndpoint(userInfo ->
                    userInfo.userService(googleUserService)
            );

            // Configure authorization endpoint: Return a login URL in JSON format
            // Frontend can use this URL to redirect the user to.
            oauth2.authorizationEndpoint(authEndpoint -> {
                authEndpoint.baseUri("/api/v1/auth/login/oauth2/authorize");
                authEndpoint.authorizationRedirectStrategy((request, response, url) -> {
                    response.setStatus(HttpStatus.OK.value());
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");

                    final GetLoginUrlResponse getLoginUrlResponse = GetLoginUrlResponse.builder()
                            .url(url)
                            .build();
                    final ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
                    final String json = ow.writeValueAsString(getLoginUrlResponse);
                    response.getWriter().write(json);
                });
                authEndpoint.authorizationRequestRepository(authorizationRequestRepository());
            });

            // Configure the redirection endpoint
            // Set a custom callback url
            oauth2.redirectionEndpoint(redirectEndpoint ->
                    redirectEndpoint.baseUri("/api/v1/auth/login/oauth2/callback/*")
            );

            // Configure the success handler
            // When the user logs in successfully, the UserResponse is returned in JSON format.
            oauth2.successHandler((request, response, authentication) -> {
                // Regenerate CSRF token after successful OAuth2 login
                final CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
                final CsrfToken newToken = tokenRepository.generateToken(request);
                tokenRepository.saveToken(newToken, request, response);
                request.setAttribute(CsrfToken.class.getName(), newToken);
                request.setAttribute("_csrf", newToken);

                response.setStatus(HttpStatus.OK.value());
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                // Get the CustomOAuth2User and extract the User entity, then convert to UserResponse
                final CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
                final UserResponse userResponse = UserResponse.fromUser(customOAuth2User.getUser());
                final ObjectWriter writer = objectMapper.writer().withDefaultPrettyPrinter();
                final String json = writer.writeValueAsString(userResponse);
                response.getWriter().write(json);
            });

            // Configure the failure handler
            // When the user fails to log in, an error message is returned in JSON format.
            oauth2.failureHandler((request, response, exception) -> {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"error\":\"" + exception.getMessage() + "\"}");
            });
        });

        // Configure logout
        // When the user logs out, the session is invalidated and a 200 OK response is returned.
        http.logout(logout -> logout
                .logoutUrl("/api/v1/auth/logout")
                .logoutSuccessHandler((request, response, authentication) ->
                        response.setStatus(HttpStatus.OK.value())
                )
        );

        // Enable CSRF protection with cookie repository for SPA
        http.csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
        );

        // Add CSRF token to response
        http.addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class);

        return http.build();
    }

    // Custom CSRF handler for SPA applications
    private static final class SpaCsrfTokenRequestHandler extends CsrfTokenRequestAttributeHandler {
        private final CsrfTokenRequestHandler delegate = new XorCsrfTokenRequestAttributeHandler();

        @Override
        public void handle(final HttpServletRequest request, final HttpServletResponse response,
                           final Supplier<CsrfToken> csrfToken) {
            this.delegate.handle(request, response, csrfToken);
        }

        @Override
        public String resolveCsrfTokenValue(final HttpServletRequest request, final CsrfToken csrfToken) {
            if (StringUtils.hasText(request.getHeader(csrfToken.getHeaderName()))) {
                return super.resolveCsrfTokenValue(request, csrfToken);
            }
            return this.delegate.resolveCsrfTokenValue(request, csrfToken);
        }
    }

    // Filter to ensure CSRF token is in cookie
    private static final class CsrfCookieFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                        final FilterChain filterChain) throws ServletException, IOException {
            CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
            csrfToken.getToken(); // Force token generation
            filterChain.doFilter(request, response);
        }
    }
}
