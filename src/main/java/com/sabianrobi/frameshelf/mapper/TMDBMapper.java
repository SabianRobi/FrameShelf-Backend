package com.sabianrobi.frameshelf.mapper;

import com.sabianrobi.frameshelf.entity.*;
import com.sabianrobi.frameshelf.entity.response.CastMemberResponse;
import com.sabianrobi.frameshelf.entity.response.SearchActorResponse;
import com.sabianrobi.frameshelf.entity.response.SearchMovieResponse;
import info.movito.themoviedbapi.model.core.popularperson.PopularPerson;
import info.movito.themoviedbapi.model.movies.Cast;
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
                .backdropPath(tmdbMovieDb.getBackdropPath())
                .belongsToCollection(collection)
                .budget(tmdbMovieDb.getBudget())
                .genres(genres)
                .homepage(tmdbMovieDb.getHomepage())
                .id(tmdbMovieDb.getId())
                .imdbId(tmdbMovieDb.getImdbID())
                .originalLanguage(tmdbMovieDb.getOriginalLanguage())
                .originalTitle(tmdbMovieDb.getOriginalTitle())
                .overview(tmdbMovieDb.getOverview())
                .popularity(tmdbMovieDb.getPopularity())
                .posterPath(tmdbMovieDb.getPosterPath())
                .productionCompanies(productionCompanies)
                .productionCountries(productionCountries)
                .releaseDate(tmdbMovieDb.getReleaseDate())
                .revenue(tmdbMovieDb.getRevenue())
                .runtime(tmdbMovieDb.getRuntime())
                .spokenLanguages(spokenLanguages)
                .status(tmdbMovieDb.getStatus())
                .tagline(tmdbMovieDb.getTagline())
                .title(tmdbMovieDb.getTitle())
                .video(tmdbMovieDb.getVideo())
                .voteAverage(tmdbMovieDb.getVoteAverage())
                .voteCount(tmdbMovieDb.getVoteCount())
                .build();
    }

    public SearchMovieResponse mapTMDBMovieToSearchMovieResponse(final info.movito.themoviedbapi.model.core.Movie tmdbMovie) {
        return SearchMovieResponse.builder()
                .id(tmdbMovie.getId())
                .title(tmdbMovie.getTitle())
                .releaseDate(tmdbMovie.getReleaseDate())
                .originalTitle(tmdbMovie.getOriginalTitle())
                .posterPath(tmdbMovie.getPosterPath())
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
                .imdbId(tmdbPerson.getImdbId())
                .knownForDepartment(tmdbPerson.getKnownForDepartment())
                .name(tmdbPerson.getName())
                .placeOfBirth(tmdbPerson.getPlaceOfBirth())
                .popularity(tmdbPerson.getPopularity())
                .profilePath(tmdbPerson.getProfilePath())
                .build();
    }


    public SearchActorResponse mapTMDBPopularPersonToSearchActorResponse(final PopularPerson tmdbPerson) {
        return SearchActorResponse.builder()
                .id(tmdbPerson.getId())
                .name(tmdbPerson.getName())
                .profilePath(tmdbPerson.getProfilePath())
                .build();
    }

    public CastMemberResponse mapTMDBCastToCastMemberResponse(final Cast cast) {
        return CastMemberResponse.builder()
                .id(cast.getId())
                .name(cast.getName())
                .originalName(cast.getOriginalName())
                .profilePath(cast.getProfilePath())
                .character(cast.getCharacter())
                .build();
    }
}
