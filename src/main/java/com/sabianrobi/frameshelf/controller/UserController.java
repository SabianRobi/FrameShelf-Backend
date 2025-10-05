package com.sabianrobi.frameshelf.controller;

import com.sabianrobi.frameshelf.entity.User;
import com.sabianrobi.frameshelf.entity.response.UserResponse;
import com.sabianrobi.frameshelf.security.CustomOAuth2User;
import com.sabianrobi.frameshelf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable final String id) {
        try {
            final UUID userId = UUID.fromString(id);
            final Optional<User> user = userService.findById(userId);
            return user.map(u -> ResponseEntity.ok(UserResponse.fromUser(u)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (final IllegalArgumentException e) {
            // Invalid UUID format
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal final CustomOAuth2User customOAuth2User) {
        if (customOAuth2User == null || customOAuth2User.getUser() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(UserResponse.fromUser(customOAuth2User.getUser()));
    }
}
