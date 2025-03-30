package com.sabianrobi.frameshelf.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @Id
    int id;

    @ManyToMany
    Set<Genre> genres;

    @ManyToOne
    Collection belongsToCollection;

    @ManyToMany
    Set<ProductionCompany> productionCompanies;

    @ManyToMany
    Set<ProductionCountry> productionCountries;

    @ManyToMany
    Set<SpokenLanguage> spokenLanguages;

    // More important fields
    String title;
    String releaseDate;
    int runtime;
    String originalTitle;
    String originalLanguage;
    String status;
    double voteAverage;
    int voteCount;

    // Less important fields
    String homepage;

    @Size(max = 1000)
    String overview;

    String imdbId;
    String posterPath;
    double popularity;
    long revenue;
    String tagline;
    boolean video;
    boolean adult;
    String backdropPath;
    int budget;
//    Set<String> originCountry;
}
