package com.sabianrobi.frameshelf.utility;

import com.sabianrobi.frameshelf.entity.User;
import com.sabianrobi.frameshelf.error.Exception.NotAuthorizedException;
import com.sabianrobi.frameshelf.security.CustomOAuth2User;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Set;
import java.util.UUID;

public abstract class Helper {
    /**
     * Filters the given ordering keys in the Pageable object according to the allowed set
     *
     * @param pageable    Spring-created Pageable object
     * @param allowedKeys Set of allowed ordering keys
     * @return A new Pageable object with only the allowed ordering keys, or unsorted if none are allowed
     */
    public static @NonNull Pageable getSafePageable(final Pageable pageable, final Set<String> allowedKeys) {
        final java.util.List<Sort.Order> orders = pageable.getSort().stream()
                .filter(order -> allowedKeys.contains(order.getProperty()))
                .toList();

        final Sort sort = orders.isEmpty() ? Sort.unsorted() : Sort.by(orders);

        return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sort
        );
    }

    /**
     * Verifies that the authenticated user is the same as the list's owner
     *
     * @param customOAuth2User The authenticated user
     * @param userId           The user ID to check against the authenticated user
     */
    public static void verifyUserHasAccessToList(final CustomOAuth2User customOAuth2User, final UUID userId) {
        // Verify the authenticated user matches the user given as the path parameter
        final User user = customOAuth2User.getUser();
        if (!user.getId().equals(userId)) {
            throw new NotAuthorizedException("User is not authorized to access the list(s)");
        }
    }
}
