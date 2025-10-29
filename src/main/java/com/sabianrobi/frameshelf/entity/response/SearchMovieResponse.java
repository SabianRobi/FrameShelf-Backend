package com.sabianrobi.frameshelf.entity.response;

import com.sabianrobi.frameshelf.entity.Collection;
import com.sabianrobi.frameshelf.entity.MovieBase;
import com.sabianrobi.frameshelf.entity.movie.ProductionCompany;
import com.sabianrobi.frameshelf.entity.movie.ProductionCountry;
import com.sabianrobi.frameshelf.entity.movie.SpokenLanguage;
import info.movito.themoviedbapi.model.core.Genre;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class SearchMovieResponse extends MovieBase {
    boolean adult;
    String backdropPath;
    Collection belongsToCollection;
    int budget;
    List<Genre> genres;
    String homepage;
    String imdbId;
    String originalLanguage;
    String overview;
    double popularity;
    Set<ProductionCompany> productionCompanies;
    Set<ProductionCountry> productionCountries;
    long revenue;
    int runtime;
    Set<SpokenLanguage> spokenLanguages;
    String status;
    String tagline;
    boolean video;
    double voteAverage;
    int voteCount;
    Credits credits;

    @Data
    @Builder
    public static class Credits {
        private List<CastMemberResponse> cast;
        private List<CrewMemberResponse> crew;
    }

    @Data
    @Builder
    public static class CastMemberResponse {
        private boolean adult;
        private String gender;
        private int id;
        private String knownForDepartment;
        private String name;
        private String originalName;
        private double popularity;
        private String profilePath;
        private int castId;
        private String character;
        private String creditId;
        private int order;
    }

    @Data
    @Builder
    public static class CrewMemberResponse {
        private boolean adult;
        private String gender;
        private int id;
        private String knownForDepartment;
        private String name;
        private String originalName;
        private double popularity;
        private String profilePath;
        private String creditId;
        private String department;
        private String job;
    }
}
