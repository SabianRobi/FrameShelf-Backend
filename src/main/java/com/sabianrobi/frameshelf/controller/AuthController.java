package com.sabianrobi.frameshelf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    /**
     * Endpoint to check if the user is authenticated
     *
     * @return User information if authenticated, 401 if not
     */
    @GetMapping("/user")
    public ResponseEntity<?> getUser() {
        // Check if the user is authenticated
        if (SecurityContextHolder.getContext().getAuthentication() instanceof OAuth2AuthenticationToken authentication) {

            // Return the user's attributes
            Map<String, Object> attributes = authentication.getPrincipal().getAttributes();
            return ResponseEntity.ok(attributes);
        }

        // Return 401 if not authenticated
        return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
    }
}