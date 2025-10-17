package com.sabianrobi.frameshelf.entity.response;

import com.sabianrobi.frameshelf.entity.MovieList;
import com.sabianrobi.frameshelf.mapper.MovieMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MovieListResponse extends ListResponse {
    private Set<MovieResponse> movies;

    /**
     * Create a MovieListResponse from a MovieList entity
     *
     * @param movieList   The MovieList entity
     * @param movieMapper The MovieMapper to use for conversion
     * @return The MovieListResponse
     */
    public static MovieListResponse fromMovieList(final MovieList movieList, final MovieMapper movieMapper) {
        if (movieList == null) {
            return null;
        }

        return MovieListResponse.builder()
                .id(movieList.getId())
                .name(movieList.getName())
                .userId(movieList.getUser() != null ? movieList.getUser().getId() : null)
                .movies(movieList.getMovies() != null && movieMapper != null ?
                        movieList.getMovies().stream()
                                .map(movieMapper::mapMovieToMovieResponse)
                                .collect(Collectors.toSet()) : null)
                .build();
    }
}
