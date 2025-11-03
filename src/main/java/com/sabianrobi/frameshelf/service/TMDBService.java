package com.sabianrobi.frameshelf.service;

import com.sabianrobi.frameshelf.entity.response.SearchMoviesResponse;
import com.sabianrobi.frameshelf.entity.response.SearchPeopleResponse;
import com.sabianrobi.frameshelf.mapper.TMDBMapper;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.core.popularperson.PopularPersonResultsPage;
import info.movito.themoviedbapi.model.movies.MovieDb;
import info.movito.themoviedbapi.model.people.PersonDb;
import info.movito.themoviedbapi.tools.TmdbException;
import info.movito.themoviedbapi.tools.appendtoresponse.MovieAppendToResponse;
import info.movito.themoviedbapi.tools.appendtoresponse.PersonAppendToResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TMDBService {
    final private TmdbApi tmdbApi;
    final private String language = "en-US";

    @Autowired
    private GenreService genreService;

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private ProductionCompanyService productionCompanyService;

    @Autowired
    private ProductionCountryService productionCountryService;

    @Autowired
    private SpokenLanguageService spokenLanguageService;

    @Autowired
    private TMDBMapper tmdbMapper;

    @Autowired
    public TMDBService(final Map<String, String> tmdbConfigs) {
        tmdbApi = new TmdbApi(tmdbConfigs.get("read-key"));
    }

    // ------------
    //    Movies
    // ------------

    public Page<SearchMoviesResponse> searchMovies(final String query, final int page) throws TmdbException {
        final TmdbSearch tmdbSearch = tmdbApi.getSearch();
        final MovieResultsPage searchResult = tmdbSearch.searchMovie(query, true, language, null, page, null, null);

        final List<SearchMoviesResponse> movieResults = searchResult.getResults().stream().map(movie -> tmdbMapper.mapTMDBMovieToSearchMoviesResponse(movie)).toList();

        final Pageable pageable = PageRequest.of(page - 1, 20);
        return new PageImpl<>(movieResults, pageable, searchResult.getTotalResults());
    }

    public MovieDb searchMovie(final Integer movieId) throws TmdbException {
        return tmdbApi.getMovies().getDetails(movieId, language, MovieAppendToResponse.CREDITS);
    }

    // ------------
    //    People
    // ------------

    public Page<SearchPeopleResponse> searchPeople(final String query, final int page) throws TmdbException {
        final TmdbSearch tmdbSearch = tmdbApi.getSearch();
        final PopularPersonResultsPage searchResult = tmdbSearch.searchPerson(query, true, language, page);

        final List<SearchPeopleResponse> movieResults = searchResult.getResults().stream().map(person -> tmdbMapper.mapTMDBPopularPersonToSearchPersonResponse(person)).toList();

        final Pageable pageable = PageRequest.of(page - 1, 20);
        return new PageImpl<>(movieResults, pageable, searchResult.getTotalResults());
    }

    public PersonDb searchPerson(final Integer personId) throws TmdbException {
        return tmdbApi.getPeople().getDetails(personId, language, PersonAppendToResponse.MOVIE_CREDITS, PersonAppendToResponse.TV_CREDITS);
    }

    // Should be moved to MovieService, just the query to TMDB should remain here
//    public Movie createMovie(final int movieId, final String watchedLanguage, final Date watchedAt) throws TmdbException {
//        final TmdbMovies tmdbMovies = tmdbApi.getMovies();
//        final MovieDb tmdbMovie = tmdbMovies.getDetails(movieId, language);
//
//        // Create & get sub models
//        final Set<Genre> genres = genreService.createGenres(tmdbMovie.getGenres());
//        final Collection collection = collectionService.createCollection(tmdbMovie.getBelongsToCollection());
//        final Set<ProductionCompany> productionCompanies = productionCompanyService.createProductionCompanies(tmdbMovie.getProductionCompanies());
//        final Set<ProductionCountry> productionCountries = productionCountryService.createProductionCountries(tmdbMovie.getProductionCountries());
//        final Set<SpokenLanguage> spokenLanguages = spokenLanguageService.createSpokenLanguages(tmdbMovie.getSpokenLanguages());
//
//        return tmdbMapper.mapTMDBMovieDbToMovie(tmdbMovie, genres, collection, productionCompanies, productionCountries, spokenLanguages, watchedLanguage, watchedAt);
//    }
//    public List<CastMemberResponse> getMovieCredits(final Integer movieId) throws TmdbException {
//        final TmdbMovies tmdbSearch = tmdbApi.getMovies();
//
//        final Credits credits = tmdbSearch.getCredits(movieId, language);
//
//        return credits.getCast().stream().map(member -> tmdbMapper.mapTMDBCastToCastMemberResponse(member)).toList();
//    }

    // ------------
    //    Actors
    // ------------

//    public Actor createActor(final int actorId) throws TmdbException {
//        final TmdbPeople tmdbPeople = tmdbApi.getPeople();
//        final PersonDb tmdbPerson = tmdbPeople.getDetails(actorId, language);
//
//        return tmdbMapper.mapTMDBActorToActor(tmdbPerson);
//    }
//
}
