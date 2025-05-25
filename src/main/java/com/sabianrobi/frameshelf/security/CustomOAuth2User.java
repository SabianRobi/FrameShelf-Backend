package com.sabianrobi.frameshelf.security;

import com.sabianrobi.frameshelf.entity.User;
import lombok.Getter;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * Custom OAuth2User implementation that wraps a standard OAuth2User
 * and adds our application's User entity.
 */
@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private final User user;

    public CustomOAuth2User(OAuth2User oauth2User, User user, String nameAttributeKey) {
        super(oauth2User.getAuthorities(), oauth2User.getAttributes(), nameAttributeKey);
        this.user = user;
    }

}