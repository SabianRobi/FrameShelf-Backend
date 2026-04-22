package com.sabianrobi.frameshelf.repository;

import com.sabianrobi.frameshelf.entity.List;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ListRepository extends CrudRepository<List, UUID> {
    java.util.List<List> findByUserId(final UUID userId);

    java.util.List<List> findByUserIdAndNameContainingIgnoreCase(final UUID userId, final String name);
}
