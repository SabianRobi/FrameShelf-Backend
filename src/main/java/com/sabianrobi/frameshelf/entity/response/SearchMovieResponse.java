package com.sabianrobi.frameshelf.entity.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchMovieResponse {
    int id;
    String title;
    String releaseDate;
    String originalTitle;
    String posterPath;
}
