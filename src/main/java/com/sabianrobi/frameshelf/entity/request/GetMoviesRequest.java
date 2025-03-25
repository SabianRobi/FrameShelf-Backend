package com.sabianrobi.frameshelf.entity.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetMoviesRequest {
    // Filtering
    private String title;

    // Pagination
    private int page;
    private int size;

    // Sorting
    private String sort;
}
