package com.sabianrobi.frameshelf.entity.response;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@Builder
public class MovieResponse {
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
    String homepage;
    String overview;
    String posterPath;
    String backdropPath;
    String watchedLanguage;
    Date watchedAt;
}
