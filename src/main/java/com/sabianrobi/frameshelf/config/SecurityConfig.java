package com.sabianrobi.frameshelf.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sabianrobi.frameshelf.entity.response.GetLoginUrlResponse;
import com.sabianrobi.frameshelf.service.GoogleUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final GoogleUserService googleUserService;

    public SecurityConfig(final GoogleUserService googleUserService) {
        this.googleUserService = googleUserService;
    }

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
                                AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/auth/login/oauth2/authorize/**"),
                                AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/auth/login/oauth2/callback/**"),
                                AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/auth/user"),
                                AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/error")
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
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
                    final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
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
            // When the user logs in successfully, the OAuth2 user information is returned in JSON format.
            oauth2.successHandler((request, response, authentication) -> {
                response.setStatus(HttpStatus.OK.value());
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                // Get the OAuth2 user
                final ObjectMapper mapper = new ObjectMapper();
                final ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
                final String json = writer.writeValueAsString(authentication.getPrincipal());
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

        // Enable CSRF protection
        http.csrf(csrf -> csrf
                .ignoringRequestMatchers(
                        // Exclude OAuth2 endpoints from CSRF protection
                        AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/auth/login/oauth2/authorize/**"),
                        AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/auth/login/oauth2/callback/**"),
                        AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/auth/user")
                )
        );

        return http.build();
    }
}
