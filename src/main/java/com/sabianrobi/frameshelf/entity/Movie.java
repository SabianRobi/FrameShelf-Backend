package com.sabianrobi.frameshelf.entity;

import com.sabianrobi.frameshelf.entity.movie.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    boolean adult;
    String backdropPath;

    @ManyToOne
    Collection belongsToCollection;

    int budget;

    @ManyToMany
    Set<Genre> genres;

    String homepage;

    @Id
    int id;

    String imdbId;
    String[] originalCountry;
    String originalLanguage;
    String originalTitle;

    @Size(max = 1000)
    String overview;

    double popularity;
    String posterPath;

    @ManyToMany
    Set<ProductionCompany> productionCompanies;

    @ManyToMany
    Set<ProductionCountry> productionCountries;

    String releaseDate;
    long revenue;
    int runtime;

    @ManyToMany
    Set<SpokenLanguage> spokenLanguages;

    String status;
    String tagline;
    String title;
    boolean video;
    double voteAverage;
    int voteCount;

    @OneToOne
    Credits credits;
}
