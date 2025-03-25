package com.sabianrobi.frameshelf.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TMDBConfig {
    @Bean
    @ConfigurationProperties(prefix = "tmdb.api")
    public Map<String, String> spotifyConfigs() {
        return new HashMap<>();
    }
}
