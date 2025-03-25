package com.sabianrobi.frameshelf.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetMoviesRequest {
    // Filtering
    private String title;

    // Pagination
    private int page;
    private int size;

    // Sorting
    private String sort;
}
