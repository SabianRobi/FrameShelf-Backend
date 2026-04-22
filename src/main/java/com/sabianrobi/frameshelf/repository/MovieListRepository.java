package com.sabianrobi.frameshelf.repository;

import com.sabianrobi.frameshelf.entity.MovieList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MovieListRepository extends CrudRepository<MovieList, UUID> {
    java.util.List<MovieList> findByUserId(final UUID userId);

    java.util.List<MovieList> findByUserIdAndNameContainingIgnoreCase(final UUID userId, final String name);
}
