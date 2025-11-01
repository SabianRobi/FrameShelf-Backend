package com.sabianrobi.frameshelf.mapper;

import com.sabianrobi.frameshelf.entity.Movie;
import com.sabianrobi.frameshelf.entity.MovieInList;
import com.sabianrobi.frameshelf.entity.response.MovieInListResponse;
import com.sabianrobi.frameshelf.entity.response.MovieResponse;
import com.sabianrobi.frameshelf.entity.response.MovieWithoutCreditsResponse;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {

    public MovieResponse mapMovieToMovieResponse(final Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
//                .genres(movie.getGenres().stream().map(Genre::getName).collect(Collectors.toSet()))
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
                .build();
    }

    public MovieWithoutCreditsResponse mapMovieToMovieWithoutCreditsResponse(final Movie movie) {
        return MovieWithoutCreditsResponse.builder()
                .adult(movie.isAdult())
                .backdropPath(movie.getBackdropPath())
                .belongsToCollection(movie.getBelongsToCollection())
                .budget(movie.getBudget())
                .genres(movie.getGenres())
                .homepage(movie.getHomepage())
                .id(movie.getId())
                .imdbId(movie.getImdbId())
                .originalCountry(movie.getOriginalCountry())
                .originalLanguage(movie.getOriginalLanguage())
                .originalTitle(movie.getOriginalTitle())
                .overview(movie.getOverview())
                .popularity(movie.getPopularity())
                .posterPath(movie.getPosterPath())
                .productionCompanies(movie.getProductionCompanies())
                .productionCountries(movie.getProductionCountries())
                .releaseDate(movie.getReleaseDate())
                .revenue(movie.getRevenue())
                .runtime(movie.getRuntime())
                .spokenLanguages(movie.getSpokenLanguages())
                .status(movie.getStatus())
                .tagline(movie.getTagline())
                .title(movie.getTitle())
                .video(movie.isVideo())
                .voteAverage(movie.getVoteAverage())
                .voteCount(movie.getVoteCount())
                .build();
    }

    public MovieInListResponse mapMovieInListToMovieInListResponse(final MovieInList movieInList) {
        final MovieWithoutCreditsResponse movie = mapMovieToMovieWithoutCreditsResponse(movieInList.getMovie());

        return MovieInListResponse.builder()
                .id(movieInList.getId())
                .movie(movie)
                .addedAt(movieInList.getAddedAt())
                .notes(movieInList.getNotes())
                .watchedAt(movieInList.getWatchedAt())
                .build();
    }
}
