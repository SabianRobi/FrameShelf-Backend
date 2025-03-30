package com.sabianrobi.frameshelf.entity.response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class MovieResponse {
    // More important fields
    int id;
    String title;
    String releaseDate;
    int runtime;
    String originalTitle;
    String originalLanguage;
    String status;
    double voteAverage;
    int voteCount;
    Set<String> genres;

    // Less important fields
    String homepage;
    String overview;
}
