package com.sabianrobi.frameshelf.repository;

import com.sabianrobi.frameshelf.entity.movie.Genre;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface GenreRepository extends CrudRepository<Genre, Integer> {
    Set<Genre> findAllByIdIn(final Set<Integer> ids);
}
