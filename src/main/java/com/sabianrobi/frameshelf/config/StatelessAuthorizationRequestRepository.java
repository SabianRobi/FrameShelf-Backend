package com.sabianrobi.frameshelf.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * A stateless implementation of AuthorizationRequestRepository that stores
 * the OAuth2AuthorizationRequest in the URL as a query parameter.
 * This allows the OAuth2 flow to work without relying on session cookies.
 */
public class StatelessAuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private static final Map<String, OAuth2AuthorizationRequest> authorizationRequestStore = new HashMap<>();

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        String state = request.getParameter("state");
        if (state != null) {
            return authorizationRequestStore.get(state);
        }
        return null;
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            removeAuthorizationRequest(request, response);
            return;
        }

        String state = authorizationRequest.getState();
        authorizationRequestStore.put(state, authorizationRequest);
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        String state = request.getParameter("state");
        if (state != null) {
            return authorizationRequestStore.remove(state);
        }
        return null;
    }
}