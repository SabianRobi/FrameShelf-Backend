package com.sabianrobi.frameshelf.service;

import com.sabianrobi.frameshelf.entity.*;
import com.sabianrobi.frameshelf.entity.response.SearchMovieResponse;
import com.sabianrobi.frameshelf.mapper.TMDBMapper;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.movies.MovieDb;
import info.movito.themoviedbapi.tools.TmdbException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class TMDBService {
    final private TmdbApi tmdbApi;

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

    // Queries the TMDB API for details
    public MovieDb getTMDBMovie(final int id) throws TmdbException {
        final TmdbMovies tmdbMovies = tmdbApi.getMovies();

        return tmdbMovies.getDetails(id, "en-US");
    }

    public Movie createMovie(final int movieId) throws TmdbException {
        final MovieDb tmdbMovie = getTMDBMovie(movieId);

        // Create & get sub models
        final Set<Genre> genres = genreService.createGenres(tmdbMovie.getGenres());
        final Collection collection = collectionService.createCollection(tmdbMovie.getBelongsToCollection());
        final Set<ProductionCompany> productionCompanies = productionCompanyService.createProductionCompanies(tmdbMovie.getProductionCompanies());
        final Set<ProductionCountry> productionCountries = productionCountryService.createProductionCountries(tmdbMovie.getProductionCountries());
        final Set<SpokenLanguage> spokenLanguages = spokenLanguageService.createSpokenLanguages(tmdbMovie.getSpokenLanguages());

        return tmdbMapper.mapTMDBMovieDbToMovie(tmdbMovie, genres, collection, productionCompanies, productionCountries, spokenLanguages);
    }

    public Page<SearchMovieResponse> searchMovie(final String query, final int page) throws TmdbException {
        final TmdbSearch tmdbSearch = tmdbApi.getSearch();
        final MovieResultsPage searchResult = tmdbSearch.searchMovie(query, true, "en-US", null, page, null, null);

        final List<SearchMovieResponse> movieResults = searchResult.getResults().stream().map(movie -> tmdbMapper.mapTMDBMovieToSearchMovieResponse(movie)).toList();

        final Pageable pageable = PageRequest.of(page - 1, 20);
        return new PageImpl<>(movieResults, pageable, searchResult.getTotalResults());
    }
}
