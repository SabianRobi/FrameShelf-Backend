package com.sabianrobi.frameshelf.entity;

import com.sabianrobi.frameshelf.entity.movie.Genre;
import com.sabianrobi.frameshelf.entity.movie.ProductionCompany;
import com.sabianrobi.frameshelf.entity.movie.ProductionCountry;
import com.sabianrobi.frameshelf.entity.movie.SpokenLanguage;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Movie extends MovieBase {
    boolean adult;
    String backdropPath;

    @ManyToOne
    Collection belongsToCollection;

    int budget;

    @ManyToMany
    Set<Genre> genres;

    String homepage;

    String imdbId;
    String originalLanguage;

    @Size(max = 1000)
    String overview;

    double popularity;

    @ManyToMany
    Set<ProductionCompany> productionCompanies;

    @ManyToMany
    Set<ProductionCountry> productionCountries;

    long revenue;
    int runtime;

    @ManyToMany
    Set<SpokenLanguage> spokenLanguages;

    String status;
    String tagline;
    boolean video;
    double voteAverage;
    int voteCount;

    // Credits
}
