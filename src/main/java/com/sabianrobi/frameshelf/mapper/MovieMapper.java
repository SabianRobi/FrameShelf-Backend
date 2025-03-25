package com.sabianrobi.frameshelf.mapper;

import com.sabianrobi.frameshelf.entity.Genre;
import com.sabianrobi.frameshelf.entity.Movie;
import com.sabianrobi.frameshelf.entity.response.MovieResponse;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MovieMapper {

    public MovieResponse mapMovieToMovieResponse(final Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .genres(movie.getGenres().stream().map(Genre::getName).collect(Collectors.toSet()))
                .vote_average(movie.getVote_average())
                .vote_count(movie.getVote_count())
                .status(movie.getStatus())
                .runtime(movie.getRuntime())
                .original_title(movie.getOriginal_title())
                .release_date(movie.getRelease_date())
                .original_language(movie.getOriginal_language())
                .homepage(movie.getHomepage())
                .overview(movie.getOverview())
                .build();
    }
}
