package com.sabianrobi.frameshelf.entity.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchMoviesResponse {
    int id;
    String overview;
    String title;
    String releaseDate;
    String originalTitle;
    String posterPath;
}
