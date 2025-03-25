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
    Collection belongs_to_collection;

    @ManyToMany
    Set<ProductionCompany> production_companies;

    @ManyToMany
    Set<ProductionCountry> production_countries;

    @ManyToMany
    Set<SpokenLanguage> spoken_languages;

    // More important fields
    String title;
    String release_date;
    int runtime;
    String original_title;
    String original_language;
    String status;
    double vote_average;
    int vote_count;

    // Less important fields
    String homepage;

    @Size(max = 1000)
    String overview;

    String imdb_id;
    String poster_path;
    double popularity;
    long revenue;
    String tagline;
    boolean video;
    boolean adult;
    String backdrop_path;
    int budget;
//    Set<String> origin_country;
}
