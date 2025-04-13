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
                .voteAverage(movie.getVoteAverage())
                .voteCount(movie.getVoteCount())
                .status(movie.getStatus())
                .runtime(movie.getRuntime())
                .originalTitle(movie.getOriginalTitle())
                .releaseDate(movie.getReleaseDate())
                .originalLanguage(movie.getOriginalLanguage())
                .homepage(movie.getHomepage())
                .overview(movie.getOverview())
                .posterPath(movie.getPosterPath())
                .backdropPath(movie.getBackdropPath())
                .watchedLanguage(movie.getWatchedLanguage())
                .watchedAt(movie.getWatchedAt())
                .build();
    }
}
