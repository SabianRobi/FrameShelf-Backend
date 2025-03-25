package com.sabianrobi.frameshelf.service;

import com.sabianrobi.frameshelf.entity.*;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.movies.MovieDb;
import info.movito.themoviedbapi.tools.TmdbException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private Map<String, String> tmdbConfigs;

    @Autowired
    public TMDBService(final Map<String, String> tmdbConfigs) {
        System.out.println(tmdbConfigs + "\n" + tmdbConfigs.get("read-key"));
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

        return Movie.builder()
                .adult(tmdbMovie.getAdult())
                .backdrop_path(tmdbMovie.getBackdropPath())
                .belongs_to_collection(collection)
                .budget(tmdbMovie.getBudget())
                .genres(genres)
                .homepage(tmdbMovie.getHomepage())
                .id(tmdbMovie.getId())
                .imdb_id(tmdbMovie.getImdbID())
                .original_language(tmdbMovie.getOriginalLanguage())
                .original_title(tmdbMovie.getOriginalTitle())
                .overview(tmdbMovie.getOverview())
                .popularity(tmdbMovie.getPopularity())
                .poster_path(tmdbMovie.getPosterPath())
                .production_companies(productionCompanies)
                .production_countries(productionCountries)
                .release_date(tmdbMovie.getReleaseDate())
                .revenue(tmdbMovie.getRevenue())
                .runtime(tmdbMovie.getRuntime())
                .spoken_languages(spokenLanguages)
                .status(tmdbMovie.getStatus())
                .tagline(tmdbMovie.getTagline())
                .title(tmdbMovie.getTitle())
                .video(tmdbMovie.getVideo())
                .vote_average(tmdbMovie.getVoteAverage())
                .vote_count(tmdbMovie.getVoteCount())
                .build();
    }

}
