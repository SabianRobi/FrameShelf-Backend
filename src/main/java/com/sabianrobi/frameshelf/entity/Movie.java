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

import java.util.Date;
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
    String status;
    String posterPath;
    String backdropPath;
    // String originCountry; // TODO: The API wrapper does not support this field

    @Size(max = 1000)
    String overview;

    // Fields that should be in the relation between Movie and User
    Date watchedAt;
    String watchedLanguage;

    // Less important fields
    String originalLanguage;
    String homepage;
    double voteAverage;
    int voteCount;

    String imdbId;
    double popularity;
    long revenue;
    String tagline;
    boolean video;
    boolean adult;
    int budget;
}
