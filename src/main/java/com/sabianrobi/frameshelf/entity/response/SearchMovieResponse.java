package com.sabianrobi.frameshelf.entity.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchMovieResponse {
    int id;
    String title;
    String release_date;
    String original_title;
    String poster_path;
}
