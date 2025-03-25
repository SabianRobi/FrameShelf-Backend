package com.sabianrobi.frameshelf.entity.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
