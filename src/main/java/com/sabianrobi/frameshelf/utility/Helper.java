package com.sabianrobi.frameshelf.utility;

import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Set;

public abstract class Helper {
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
}
