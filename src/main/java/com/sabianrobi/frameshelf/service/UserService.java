package com.sabianrobi.frameshelf.service;

import com.sabianrobi.frameshelf.entity.GoogleUser;
import com.sabianrobi.frameshelf.entity.User;
import com.sabianrobi.frameshelf.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findById(final UUID id) {
        return userRepository.findById(id);
    }

    public User findOrCreateUserForGoogleUser(final GoogleUser googleUser) {
        // Try to find an existing user linked to this GoogleUser
        Optional<User> existingUser = userRepository.findByGoogleUser(googleUser);

        if (existingUser.isPresent()) {
            // Update last login time
            User user = existingUser.get();
            user.setLastLoginAt(LocalDateTime.now());
            return userRepository.save(user);
        } else {
            // Create a new user
            // Generate a username based on email (before the @ symbol)
            String email = googleUser.getEmail();
            String baseUsername = email.substring(0, email.indexOf('@'));

            // Check if username exists, if so, append a number
            String username = baseUsername;
            int counter = 1;
            while (userRepository.findByUsername(username).isPresent()) {
                username = baseUsername + counter++;
            }

            User newUser = User.builder()
                    .username(username)
                    .displayName(googleUser.getFullName())
                    .profilePicture(googleUser.getPicture())
                    .googleUser(googleUser)
                    .createdAt(LocalDateTime.now())
                    .lastLoginAt(LocalDateTime.now())
                    .build();

            return userRepository.save(newUser);
        }
    }
}