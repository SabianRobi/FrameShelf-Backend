package com.sabianrobi.frameshelf.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration class for Jackson ObjectMapper.
 * Registers the JavaTimeModule to handle Java 8 date/time types like LocalDateTime.
 */
@Configuration
public class JacksonConfig {

    /**
     * Creates and configures the ObjectMapper bean.
     * Registers the JavaTimeModule to handle Java 8 date/time types.
     *
     * @return the configured ObjectMapper
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}