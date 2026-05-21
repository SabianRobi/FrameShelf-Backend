package com.sabianrobi.frameshelf.controller;

import com.sabianrobi.frameshelf.entity.User;
import com.sabianrobi.frameshelf.entity.response.UserResponse;
import com.sabianrobi.frameshelf.security.CustomOAuth2User;
import com.sabianrobi.frameshelf.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable final UUID userId) {
        try {
            final Optional<User> user = userService.findById(userId);
            return user.map(u -> ResponseEntity.ok(UserResponse.fromUser(u)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (final IllegalArgumentException e) {
            // Invalid UUID format
            System.err.println(e.getMessage());
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

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Authentication authentication,
            @PathVariable("userId") final UUID userId,
            @AuthenticationPrincipal final CustomOAuth2User customOAuth2User
    ) {
        userService.deleteUser(userId, customOAuth2User.getUser());

        // Invalidate the current session
        new SecurityContextLogoutHandler().logout(request, response, authentication);

        return ResponseEntity.noContent().build();
    }
}
