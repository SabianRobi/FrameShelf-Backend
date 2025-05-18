package com.sabianrobi.frameshelf.service;

import com.sabianrobi.frameshelf.entity.GoogleUser;
import com.sabianrobi.frameshelf.repository.GoogleUserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class GoogleUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final GoogleUserRepository googleUserRepository;
    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    public GoogleUserService(final GoogleUserRepository googleUserRepository) {
        this.googleUserRepository = googleUserRepository;
    }

    @Override
    public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // Get the OAuth2User from the default service
        final OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // Extract user details from OAuth2User
        final Map<String, Object> attributes = oAuth2User.getAttributes();
        final String email = (String) attributes.get("email");
        final String name = (String) attributes.get("name");
        final String googleId = (String) attributes.get("sub");
        final String picture = (String) attributes.get("picture");

        // Check if a user exists by googleId
        final Optional<GoogleUser> existingUser = googleUserRepository.findByGoogleId(googleId);

        final GoogleUser googleUser;
        final LocalDateTime now = LocalDateTime.now();

        if (existingUser.isPresent()) {
            // Update the existing user's last login time
            googleUser = existingUser.get();
            googleUser.setLastLoginAt(now);
        } else {
            // Create a new user
            googleUser = GoogleUser.builder()
                    .email(email)
                    .fullName(name)
                    .googleId(googleId)
                    .picture(picture)
                    .createdAt(now)
                    .lastLoginAt(now)
                    .build();
        }

        // Save user to the database
        googleUserRepository.save(googleUser);

        // Return the original OAuth2User
        return oAuth2User;
    }
}