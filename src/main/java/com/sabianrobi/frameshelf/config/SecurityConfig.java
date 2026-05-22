package com.sabianrobi.frameshelf.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.frontend.url}")
    private String[] frontendUrls;

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(frontendUrls));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    SecurityFilterChain securityFilterChain(final HttpSecurity http) {
        final String frontendUrl = frontendUrls[0];

        http
                .cors(Customizer.withDefaults())
                .headers(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/login/oauth2/authorize/**",
                                "/api/v1/auth/login/oauth2/callback/**",
                                "/api/v1/auth/logout",
                                "/api/v1/user/me",
                                "/error"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                );

        // Configure OAuth2 login
        http.oauth2Login(oauth2 -> {
            oauth2.authorizationEndpoint(authEndpoint ->
                    authEndpoint.baseUri("/api/v1/auth/login/oauth2/authorize"));

            // Configure the redirection endpoint
            oauth2.redirectionEndpoint(redirectEndpoint ->
                    redirectEndpoint.baseUri("/api/v1/auth/login/oauth2/callback/*")
            );

            // Configure the success handler
            oauth2.successHandler(
                    (request, response, authentication) ->
                            response.sendRedirect(frontendUrl + "?login=success"));

            // Configure the failure handler
            // When the user fails to log in, an error message is returned in JSON format.
            oauth2.failureHandler(
                    (request, response, exception) ->
                            response.sendRedirect(frontendUrl + "?login=failure"));
        });

        // Configure logout
        http.logout(logout -> logout
                .logoutUrl("/api/v1/auth/logout")
                .logoutSuccessHandler(
                        (request, response, authentication) ->
                                response.setStatus(HttpStatus.OK.value())
                ));
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
