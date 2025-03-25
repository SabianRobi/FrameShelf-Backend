package com.sabianrobi.frameshelf.mapper;

import com.sabianrobi.frameshelf.entity.*;
import com.sabianrobi.frameshelf.entity.response.SearchActorResponse;
import com.sabianrobi.frameshelf.entity.response.SearchMovieResponse;
import info.movito.themoviedbapi.model.core.popularperson.PopularPerson;
import info.movito.themoviedbapi.model.movies.MovieDb;
import info.movito.themoviedbapi.model.people.PersonDb;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TMDBMapper {
    public Movie mapTMDBMovieDbToMovie(final MovieDb tmdbMovieDb,
                                       final Set<Genre> genres,
                                       final Collection collection,
                                       final Set<ProductionCompany> productionCompanies,
                                       final Set<ProductionCountry> productionCountries,
                                       final Set<SpokenLanguage> spokenLanguages
    ) {
        return Movie.builder()
                .adult(tmdbMovieDb.getAdult())
                .backdrop_path(tmdbMovieDb.getBackdropPath())
                .belongs_to_collection(collection)
                .budget(tmdbMovieDb.getBudget())
                .genres(genres)
                .homepage(tmdbMovieDb.getHomepage())
                .id(tmdbMovieDb.getId())
                .imdb_id(tmdbMovieDb.getImdbID())
                .original_language(tmdbMovieDb.getOriginalLanguage())
                .original_title(tmdbMovieDb.getOriginalTitle())
                .overview(tmdbMovieDb.getOverview())
                .popularity(tmdbMovieDb.getPopularity())
                .poster_path(tmdbMovieDb.getPosterPath())
                .production_companies(productionCompanies)
                .production_countries(productionCountries)
                .release_date(tmdbMovieDb.getReleaseDate())
                .revenue(tmdbMovieDb.getRevenue())
                .runtime(tmdbMovieDb.getRuntime())
                .spoken_languages(spokenLanguages)
                .status(tmdbMovieDb.getStatus())
                .tagline(tmdbMovieDb.getTagline())
                .title(tmdbMovieDb.getTitle())
                .video(tmdbMovieDb.getVideo())
                .vote_average(tmdbMovieDb.getVoteAverage())
                .vote_count(tmdbMovieDb.getVoteCount())
                .build();
    }

    public SearchMovieResponse mapTMDBMovieToSearchMovieResponse(final info.movito.themoviedbapi.model.core.Movie tmdbMovie) {
        return SearchMovieResponse.builder()
                .id(tmdbMovie.getId())
                .title(tmdbMovie.getTitle())
                .release_date(tmdbMovie.getReleaseDate())
                .original_title(tmdbMovie.getOriginalTitle())
                .poster_path(tmdbMovie.getPosterPath())
                .build();
    }

    public Actor mapTMDBActorToActor(final PersonDb tmdbPerson) {
        return Actor.builder()
                .adult(tmdbPerson.getAdult())
                .biography(tmdbPerson.getBiography())
                .birthday(tmdbPerson.getBirthday())
                .deathday(tmdbPerson.getDeathDay())
                .gender(tmdbPerson.getGender().toValue())
                .homepage(tmdbPerson.getHomepage())
                .id(tmdbPerson.getId())
                .imdb_id(tmdbPerson.getImdbId())
                .known_for_department(tmdbPerson.getKnownForDepartment())
                .name(tmdbPerson.getName())
                .place_of_birth(tmdbPerson.getPlaceOfBirth())
                .popularity(tmdbPerson.getPopularity())
                .profile_path(tmdbPerson.getProfilePath())
                .build();
    }


    public SearchActorResponse mapTMDBPopularPersonToSearchActorResponse(final PopularPerson tmdbPerson) {
        return SearchActorResponse.builder()
                .id(tmdbPerson.getId())
                .name(tmdbPerson.getName())
                .profile_path(tmdbPerson.getProfilePath())
                .build();
    }
}
