package com.sabianrobi.frameshelf.security;

import com.sabianrobi.frameshelf.entity.User;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom OAuth2User implementation that wraps a standard OAuth2User
 * and adds our application's User entity.
 */
@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private final User user;

    public CustomOAuth2User(OAuth2User oauth2User, User user, String nameAttributeKey) {
        super(
                oauth2User != null ? oauth2User.getAuthorities() : Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
                oauth2User != null ? oauth2User.getAttributes() : createAttributesFromUser(user),
                nameAttributeKey
        );
        this.user = user;
    }

    /**
     * Create attributes map from User entity when OAuth2User is not available (JWT authentication)
     */
    private static Map<String, Object> createAttributesFromUser(User user) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", user.getId().toString());
        attributes.put("email", user.getGoogleUser() != null ? user.getGoogleUser().getEmail() : "");
        attributes.put("name", user.getDisplayName());
        attributes.put("picture", user.getProfilePicture());
        return attributes;
    }
}
