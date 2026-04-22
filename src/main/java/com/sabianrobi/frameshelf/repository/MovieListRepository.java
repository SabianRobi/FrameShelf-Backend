package com.sabianrobi.frameshelf.repository;

import com.sabianrobi.frameshelf.entity.MovieList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MovieListRepository extends CrudRepository<MovieList, UUID> {
    Page<MovieList> findByUserId(final UUID userId, final Pageable pageable);

    Page<MovieList> findByUserIdAndNameContainingIgnoreCase(final UUID userId, final String name, final Pageable pageable);
}
