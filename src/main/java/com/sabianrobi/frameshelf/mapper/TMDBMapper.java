package com.sabianrobi.frameshelf.mapper;

import com.sabianrobi.frameshelf.entity.Collection;
import com.sabianrobi.frameshelf.entity.Movie;
import com.sabianrobi.frameshelf.entity.movie.*;
import com.sabianrobi.frameshelf.entity.response.SearchMovieResponse;
import com.sabianrobi.frameshelf.entity.response.SearchMoviesResponse;
import com.sabianrobi.frameshelf.entity.response.SearchPeopleResponse;
import com.sabianrobi.frameshelf.entity.response.SearchPersonResponse;
import info.movito.themoviedbapi.model.core.Language;
import info.movito.themoviedbapi.model.core.popularperson.PopularPerson;
import info.movito.themoviedbapi.model.movies.BelongsToCollection;
import info.movito.themoviedbapi.model.movies.Cast;
import info.movito.themoviedbapi.model.movies.Crew;
import info.movito.themoviedbapi.model.movies.MovieDb;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TMDBMapper {
    public SearchMoviesResponse mapTMDBMovieToSearchMoviesResponse(final info.movito.themoviedbapi.model.core.Movie tmdbMovie) {
        return SearchMoviesResponse.builder()
                .id(tmdbMovie.getId())
                .title(tmdbMovie.getTitle())
                .releaseDate(tmdbMovie.getReleaseDate())
                .originalTitle(tmdbMovie.getOriginalTitle())
                .posterPath(tmdbMovie.getPosterPath())
                .overview(tmdbMovie.getOverview())
                .build();
    }

    public SearchMovieResponse mapTMDBMovieToSearchMovieResponse(final MovieDb tmdbMovie) {
        final Set<CastMember> cast = tmdbMovie.getCredits().getCast().stream().map(c -> CastMember.builder()
                .adult(c.getAdult())
                .gender(c.getGender().toString())
                .id(c.getId())
                .knownForDepartment(c.getKnownForDepartment())
                .name(c.getName())
                .originalName(c.getOriginalName())
                .popularity(c.getPopularity())
                .profilePath(c.getProfilePath())
                .castId(c.getCastId())
                .character(c.getCharacter())
                .creditId(c.getCreditId())
                .order(c.getOrder())
                .build()).collect(Collectors.toSet());

        final Set<CrewMember> crew = tmdbMovie.getCredits().getCrew().stream().map(c -> CrewMember.builder()
                .adult(c.getAdult())
                .gender(c.getGender().toString())
                .id(c.getId())
                .knownForDepartment(c.getKnownForDepartment())
                .name(c.getName())
                .originalName(c.getOriginalName())
                .popularity(c.getPopularity())
                .profilePath(c.getProfilePath())
                .creditId(c.getCreditId())
                .department(c.getDepartment())
                .job(c.getJob())
                .build()).collect(Collectors.toSet());

        final Credits credits = Credits.builder()
                .cast(cast)
                .crew(crew)
                .build();

        return SearchMovieResponse.builder()
                .backdropPath(tmdbMovie.getBackdropPath())
                .genres(tmdbMovie.getGenres())
                .homepage(tmdbMovie.getHomepage())
                .id(tmdbMovie.getId())
                .originalLanguage(tmdbMovie.getOriginalLanguage())
                .originalTitle(tmdbMovie.getOriginalTitle())
                .overview(tmdbMovie.getOverview())
                .posterPath(tmdbMovie.getPosterPath())
                .releaseDate(tmdbMovie.getReleaseDate())
                .runtime(tmdbMovie.getRuntime())
                .status(tmdbMovie.getStatus())
                .title(tmdbMovie.getTitle())
                .voteAverage(tmdbMovie.getVoteAverage())
                .voteCount(tmdbMovie.getVoteCount())
                .credits(credits)
                .build();
    }

    public SearchPeopleResponse mapTMDBPopularPersonToSearchPersonResponse(final PopularPerson tmdbPerson) {
        final Set<SearchPeopleResponse.KnownFor> knownFor = tmdbPerson.getKnownFor().stream().map(kf -> SearchPeopleResponse.KnownFor.builder()
                .adult(kf.getAdult())
                .backdropPath(kf.getBackdropPath())
                .id(kf.getId())
                .title(kf.getTitle())
                .originalLanguage(kf.getOriginalLanguage())
                .originalTitle(kf.getOriginalTitle())
                .overview(kf.getOverview())
                .posterPath(kf.getPosterPath())
                .mediaType(kf.getMediaType())
                .popularity(kf.getPopularity())
                .releaseDate(kf.getReleaseDate())
                .video(Optional.ofNullable(kf.getVideo()).orElse(false))
                .voteAverage(kf.getVoteAverage())
                .voteCount(kf.getVoteCount())
                .build()).collect(Collectors.toSet());

        return SearchPeopleResponse.builder()
                .adult(tmdbPerson.getAdult())
                .gender(tmdbPerson.getGender().toString())
                .id(tmdbPerson.getId())
                .knownForDepartment(tmdbPerson.getKnownForDepartment())
                .name(tmdbPerson.getName())
                .originalName(tmdbPerson.getOriginalName())
                .popularity(tmdbPerson.getPopularity())
                .profilePath(tmdbPerson.getProfilePath())
                .knownFor(knownFor)
                .build();
    }

    public SearchPersonResponse mapTMDBPersonToSearchPersonResponse(final info.movito.themoviedbapi.model.people.PersonDb tmdbPerson) {
        final List<SearchPersonResponse.MovieCastMember> movieCastMembers = tmdbPerson.getMovieCredits().getCast().stream().map(c -> SearchPersonResponse.MovieCastMember.builder()
                .adult(c.getAdult())
                .backdropPath(c.getBackdropPath())
                .character(c.getCharacter())
                .creditId(c.getCreditId())
                .id(c.getId())
                .order(c.getOrder())
                .originalLanguage(c.getOriginalLanguage())
                .originalTitle(c.getOriginalTitle())
                .overview(c.getOverview())
                .popularity(c.getPopularity())
                .posterPath(c.getPosterPath())
                .releaseDate(c.getReleaseDate())
                .title(c.getTitle())
                .video(c.getVideo())
                .voteAverage(c.getVoteAverage())
                .voteCount(c.getVoteCount())
                .build()).toList();

        final List<SearchPersonResponse.MovieCrewMember> movieCrewMembers = tmdbPerson.getMovieCredits().getCrew().stream().map(c -> SearchPersonResponse.MovieCrewMember.builder()
                .adult(c.getAdult())
                .backdropPath(c.getBackdropPath())
                .creditId(c.getCreditId())
                .department(c.getDepartment())
                .id(c.getId())
                .job(c.getJob())
                .originalLanguage(c.getOriginalLanguage())
                .originalTitle(c.getOriginalTitle())
                .overview(c.getOverview())
                .popularity(c.getPopularity())
                .posterPath(c.getPosterPath())
                .releaseDate(c.getReleaseDate())
                .title(c.getTitle())
                .video(c.getVideo())
                .voteAverage(c.getVoteAverage())
                .voteCount(c.getVoteCount())
                .build()).toList();

        final SearchPersonResponse.MovieCredits movieCredits = SearchPersonResponse.MovieCredits.builder()
                .cast(movieCastMembers)
                .crew(movieCrewMembers)
                .build();

        final List<SearchPersonResponse.TvCastMember> tvCastMembers = tmdbPerson.getTvCredits().getCast().stream().map(c -> SearchPersonResponse.TvCastMember.builder()
                .adult(c.getAdult())
                .backdropPath(c.getBackdropPath())
                .character(c.getCharacter())
                .creditId(c.getCreditId())
                .episodeCount(c.getEpisodeCount())
                .firstAirDate(c.getFirstAirDate())
//                .firstCreditAirDate(c.getFirstCreditAirDate())
                .id(c.getId())
                .name(c.getName())
                .originalLanguage(c.getOriginalLanguage())
                .originalName(c.getOriginalName())
                .originCountry(c.getOriginCountry())
                .overview(c.getOverview())
                .popularity(c.getPopularity())
                .posterPath(c.getPosterPath())
                .voteAverage(c.getVoteAverage())
                .voteCount(c.getVoteCount())
                .build()).toList();

        final List<SearchPersonResponse.TvCrewMember> tvCrewMembers = tmdbPerson.getTvCredits().getCrew().stream().map(c -> SearchPersonResponse.TvCrewMember.builder()
                .adult(c.getAdult())
                .backdropPath(c.getBackdropPath())
                .creditId(c.getCreditId())
                .department(c.getDepartment())
                .episodeCount(c.getEpisodeCount())
                .firstAirDate(c.getFirstAirDate())
//                .firstCreditAirDate(c.getFirstCreditAirDate())
                .id(c.getId())
                .job(c.getJob())
                .name(c.getName())
                .originalLanguage(c.getOriginalLanguage())
                .originalName(c.getOriginalName())
                .originCountry(c.getOriginCountry())
                .overview(c.getOverview())
                .popularity(c.getPopularity())
                .posterPath(c.getPosterPath())
                .voteAverage(c.getVoteAverage())
                .voteCount(c.getVoteCount())
                .build()).toList();

        final SearchPersonResponse.TvCredits tvCredits = SearchPersonResponse.TvCredits.builder()
                .cast(tvCastMembers)
                .crew(tvCrewMembers)
                .build();

        return SearchPersonResponse.builder()
                .adult(tmdbPerson.getAdult())
                .alsoKnownAs(tmdbPerson.getAlsoKnownAs())
                .biography(tmdbPerson.getBiography())
                .birthday(tmdbPerson.getBirthday())
                .deathday(tmdbPerson.getDeathDay())
                .gender(tmdbPerson.getGender() != null ? tmdbPerson.getGender().ordinal() : 0)
                .homepage(tmdbPerson.getHomepage())
                .id(tmdbPerson.getId())
                .imdbId(tmdbPerson.getImdbId())
                .knownForDepartment(tmdbPerson.getKnownForDepartment())
                .name(tmdbPerson.getName())
                .placeOfBirth(tmdbPerson.getPlaceOfBirth())
                .popularity(tmdbPerson.getPopularity())
                .profilePath(tmdbPerson.getProfilePath())
                .movieCredits(movieCredits)
                .tvCredits(tvCredits)
                .build();
    }

    public Movie mapTMDBMovieToMovie(final MovieDb movieDb,
                                     final Collection belongsToCollection,
                                     final Set<Genre> genres,
                                     final Set<ProductionCompany> productionCompanies,
                                     final Set<ProductionCountry> productionCountries,
                                     final Set<SpokenLanguage> spokenLanguages,
                                     final Credits credits) {
        return Movie.builder()
                .adult(movieDb.getAdult())
                .backdropPath(movieDb.getBackdropPath())
                .belongsToCollection(belongsToCollection)
                .budget(movieDb.getBudget())
                .genres(genres)
                .homepage(movieDb.getHomepage())
                .id(movieDb.getId())
                .imdbId(movieDb.getImdbID())
//                .originalCountry(movieDb.getOriginalCountry())
                .originalLanguage(movieDb.getOriginalLanguage())
                .originalTitle(movieDb.getOriginalTitle())
                .overview(movieDb.getOverview())
                .popularity(movieDb.getPopularity())
                .posterPath(movieDb.getPosterPath())
                .productionCompanies(productionCompanies)
                .productionCountries(productionCountries)
                .releaseDate(movieDb.getReleaseDate())
                .revenue(movieDb.getRevenue())
                .runtime(movieDb.getRuntime())
                .spokenLanguages(spokenLanguages)
                .status(movieDb.getStatus())
                .tagline(movieDb.getTagline())
                .title(movieDb.getTitle())
                .video(movieDb.getVideo())
                .voteAverage(movieDb.getVoteAverage())
                .voteCount(movieDb.getVoteCount())
                .credits(credits)
                .build();
    }

    public Collection mapTMDBCollectionToCollection(final BelongsToCollection belongsToCollection) {
        if (belongsToCollection == null) {
            return null;
        }

        return Collection.builder()
                .id(belongsToCollection.getId())
                .name(belongsToCollection.getName())
                .posterPath(belongsToCollection.getPosterPath())
                .backdropPath(belongsToCollection.getBackdropPath())
                .build();
    }

    public Genre mapTMDBGenreToGenre(final info.movito.themoviedbapi.model.core.Genre genre) {
        return Genre.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }

    public ProductionCompany mapTMDBProductionCompanyToProductionCompany(
            final info.movito.themoviedbapi.model.core.ProductionCompany company) {
        return ProductionCompany.builder()
                .id(company.getId())
                .logoPath(company.getLogoPath())
                .name(company.getName())
                .originCountry(company.getOriginCountry())
                .build();
    }

    public ProductionCountry mapTMDBProductionCountryToProductionCountry(
            final info.movito.themoviedbapi.model.core.ProductionCountry country) {
        return ProductionCountry.builder()
                .iso31661(country.getIsoCode())
                .name(country.getName())
                .build();
    }

    public SpokenLanguage mapTMDBSpokenLanguageToSpokenLanguage(final Language language) {
        return SpokenLanguage.builder()
                .iso6391(language.getIso6391())
                .name(language.getName())
                .englishName(language.getEnglishName())
                .build();
    }

    public CastMember mapTMDBCastMemberToCastMember(final Cast cast) {
        return CastMember.builder()
                .adult(cast.getAdult())
                .gender(cast.getGender().toString())
                .id(cast.getId())
                .knownForDepartment(cast.getKnownForDepartment())
                .name(cast.getName())
                .originalName(cast.getOriginalName())
                .popularity(cast.getPopularity())
                .profilePath(cast.getProfilePath())
                .castId(cast.getCastId())
                .character(cast.getCharacter())
                .creditId(cast.getCreditId())
                .order(cast.getOrder())
                .build();
    }

    public CrewMember mapTMDBCrewMemberToCrewMember(final Crew crew) {
        return CrewMember.builder()
                .adult(crew.getAdult())
                .gender(crew.getGender().toString())
                .id(crew.getId())
                .knownForDepartment(crew.getKnownForDepartment())
                .name(crew.getName())
                .originalName(crew.getOriginalName())
                .popularity(crew.getPopularity())
                .profilePath(crew.getProfilePath())
                .creditId(crew.getCreditId())
                .department(crew.getDepartment())
                .job(crew.getJob())
                .build();
    }
}
