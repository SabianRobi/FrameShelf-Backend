package com.sabianrobi.frameshelf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
//    @Autowired
//    private GoogleCredentialService GoogleCredentialService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(registry ->
                        registry
                                .anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .build();
//                .authorizeHttpRequests((auth) ->
//                        auth.requestMatchers(
//                                        AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/"),
//                                        AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/"),
//                                        AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/auth/login"),
//                                        AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/error")
//                                ).permitAll()
//                                .anyRequest().authenticated()
//                )
//                .oauth2Login(oauth2 ->
//                        oauth2.userInfoEndpoint(user ->
//                                        user.userService(GoogleCredentialService)
//                                )
//                                .defaultSuccessUrl("/api/v1/films")
//                )
//                .logout(logout -> logout.logoutSuccessUrl("/"))
//                .exceptionHandling(exception -> exception
//                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
//                )
//                .csrf(AbstractHttpConfigurer::disable)
//                .build();
    }
}
