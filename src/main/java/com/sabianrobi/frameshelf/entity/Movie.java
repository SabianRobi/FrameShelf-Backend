package com.sabianrobi.frameshelf.entity;

import com.sabianrobi.frameshelf.entity.movie.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    private boolean adult;
    private String backdropPath;

    @ManyToOne
    private Collection belongsToCollection;

    private int budget;

    @ManyToMany
    private Set<Genre> genres;

    private String homepage;

    @Id
    private int id;

    private String imdbId;

    @ElementCollection
    private List<String> originalCountry;

    private String originalLanguage;
    private String originalTitle;

    @Size(max = 1000)
    private String overview;

    private double popularity;
    private String posterPath;

    @ManyToMany
    private Set<ProductionCompany> productionCompanies;

    @ManyToMany
    private Set<ProductionCountry> productionCountries;

    private String releaseDate;
    private long revenue;
    private int runtime;

    @ManyToMany
    private Set<SpokenLanguage> spokenLanguages;

    private String status;
    private String tagline;
    private String title;
    private boolean video;
    private double voteAverage;
    private int voteCount;

    @OneToOne
    private Credits credits;
}
