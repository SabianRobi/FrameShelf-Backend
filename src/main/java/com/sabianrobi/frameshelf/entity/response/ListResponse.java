package com.sabianrobi.frameshelf.entity.response;

import com.sabianrobi.frameshelf.entity.ActorList;
import com.sabianrobi.frameshelf.entity.MovieList;
import com.sabianrobi.frameshelf.mapper.ActorMapper;
import com.sabianrobi.frameshelf.mapper.MovieMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class ListResponse {
    private UUID id;
    private String name;
    private UUID userId;

    /**
     * Create a ListResponse from a List entity
     *
     * @param list        The List entity
     * @param movieMapper The MovieMapper to use for conversion
     * @param actorMapper The ActorMapper to use for conversion
     * @return The ListResponse
     */
    public static ListResponse fromList(com.sabianrobi.frameshelf.entity.List list,
                                        MovieMapper movieMapper,
                                        ActorMapper actorMapper) {
        if (list == null) {
            return null;
        }

        if (list instanceof MovieList) {
            return MovieListResponse.fromMovieList((MovieList) list, movieMapper);
        } else if (list instanceof ActorList) {
            return ActorListResponse.fromActorList((ActorList) list, actorMapper);
        }

        throw new IllegalArgumentException("Unknown list type: " + list.getClass().getName());
    }
}

