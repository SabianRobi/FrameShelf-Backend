package com.sabianrobi.frameshelf.entity.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetActorsRequest {
    // Filtering
    private String name;

    // Pagination
    private int page;
    private int size;

    // Sorting
    private String sort;
}
