package com.sabianrobi.frameshelf.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sabianrobi.frameshelf.entity.response.GetLoginUrlResponse;
import com.sabianrobi.frameshelf.entity.response.UserResponse;
import com.sabianrobi.frameshelf.security.CustomOAuth2User;
import com.sabianrobi.frameshelf.security.JwtAuthenticationFilter;
import com.sabianrobi.frameshelf.service.GoogleUserService;
import com.sabianrobi.frameshelf.service.JwtService;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private GoogleUserService googleUserService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

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
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                );

        // Add JWT authentication filter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

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
            // When the user logs in successfully, generate JWT token and return UserResponse in JSON format.
            oauth2.successHandler((request, response, authentication) -> {
                // Get the CustomOAuth2User and extract the User entity
                final CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
                final var user = customOAuth2User.getUser();

                // Generate JWT token
                final String jwtToken = jwtService.generateToken(
                        user.getId(),
                        user.getGoogleUser() != null ? user.getGoogleUser().getEmail() : "",
                        user.getDisplayName()
                );

                // Set JWT token as HTTP-only cookie
                final Cookie jwtCookie = new Cookie("AUTH_TOKEN", jwtToken);
                jwtCookie.setHttpOnly(true);
                jwtCookie.setSecure(false); // TODO Set to true in production with HTTPS
                jwtCookie.setPath("/");
                jwtCookie.setMaxAge((int) (jwtService.getExpirationTime() / 1000)); // Convert to seconds
                jwtCookie.setAttribute("SameSite", "Lax");
                response.addCookie(jwtCookie);

                response.setStatus(HttpStatus.OK.value());
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                // Convert to UserResponse and return
                final UserResponse userResponse = UserResponse.fromUser(user);
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
        // When the user logs out, clear JWT cookie and return 200 OK response.
        http.logout(logout -> logout
                .logoutUrl("/api/v1/auth/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    // Clear the JWT cookie
                    final Cookie jwtCookie = new Cookie("AUTH_TOKEN", null);
                    jwtCookie.setHttpOnly(true);
                    jwtCookie.setSecure(false);
                    jwtCookie.setPath("/");
                    jwtCookie.setMaxAge(0);
                    response.addCookie(jwtCookie);

                    response.setStatus(HttpStatus.OK.value());
                })
        );

        // Disable CSRF for API endpoints that use JWT authentication
        // JWT in HttpOnly cookies with SameSite=Lax provides sufficient CSRF protection
        // Keep CSRF enabled for OAuth2 endpoints if needed
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

}
