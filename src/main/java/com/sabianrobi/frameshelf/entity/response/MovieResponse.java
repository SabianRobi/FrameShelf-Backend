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
    String release_date;
    int runtime;
    String original_title;
    String original_language;
    String status;
    double vote_average;
    int vote_count;
    Set<String> genres;

    // Less important fields
    String homepage;
    String overview;
}
