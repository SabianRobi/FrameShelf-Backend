package com.sabianrobi.frameshelf.entity.response;

import com.sabianrobi.frameshelf.entity.Collection;
import com.sabianrobi.frameshelf.entity.movie.Genre;
import com.sabianrobi.frameshelf.entity.movie.ProductionCompany;
import com.sabianrobi.frameshelf.entity.movie.ProductionCountry;
import com.sabianrobi.frameshelf.entity.movie.SpokenLanguage;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class MovieWithoutCreditsResponse {
    boolean adult;
    String backdropPath;
    Collection belongsToCollection;
    int budget;
    Set<Genre> genres;
    String homepage;
    int id;
    String imdbId;
    String[] originalCountry;
    String originalLanguage;
    String originalTitle;
    String overview;
    double popularity;
    String posterPath;
    Set<ProductionCompany> productionCompanies;
    Set<ProductionCountry> productionCountries;
    String releaseDate;
    long revenue;
    int runtime;
    Set<SpokenLanguage> spokenLanguages;
    String status;
    String tagline;
    String title;
    boolean video;
    double voteAverage;
    int voteCount;
}
