package com.sabianrobi.frameshelf.service;

import com.sabianrobi.frameshelf.entity.movie.Genre;
import com.sabianrobi.frameshelf.repository.GenreRepository;
import info.movito.themoviedbapi.model.core.IdElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GenreService {
    @Autowired
    private GenreRepository repository;

    public Set<Genre> createGenres(final List<info.movito.themoviedbapi.model.core.Genre> tmdbGenres) {
        if (tmdbGenres.isEmpty()) {
            return new HashSet<>();
        }

        // Save new genres
        final ArrayList<Genre> newGenres = new ArrayList<>();

        tmdbGenres.forEach(genre -> {
            if (!repository.existsById(genre.getId())) {
                final Genre newGenre = Genre.builder()
                        .id(genre.getId())
                        .name(genre.getName())
                        .build();

                newGenres.add(newGenre);
            }
        });

        if (!newGenres.isEmpty()) {
            repository.saveAll(newGenres);
        }

        // Get all genres and return
        return repository.findAllByIdIn(tmdbGenres.stream().map(IdElement::getId).collect(Collectors.toSet()));
    }
}
