package com.sabianrobi.frameshelf.repository;

import com.sabianrobi.frameshelf.entity.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ListRepository extends CrudRepository<List, UUID> {
    Page<List> findByUserId(final UUID userId, final Pageable pageable);

    Page<List> findByUserIdAndNameContainingIgnoreCase(final UUID userId, final String name, final Pageable pageable);
}
