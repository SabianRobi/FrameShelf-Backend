package com.sabianrobi.frameshelf.service;

import com.sabianrobi.frameshelf.entity.Collection;
import com.sabianrobi.frameshelf.repository.CollectionRepository;
import info.movito.themoviedbapi.model.movies.BelongsToCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollectionService {
    @Autowired
    private CollectionRepository repository;

    public Collection createCollection(final BelongsToCollection tmdbCollection) {
        if (tmdbCollection == null) {
            return null;
        }

        final Collection collection = Collection.builder()
                .id(tmdbCollection.getId())
                .name(tmdbCollection.getName())
                .posterPath(tmdbCollection.getPosterPath())
                .backdropPath(tmdbCollection.getBackdropPath())
                .build();

        return repository.save(collection);
    }
}
