package com.sabianrobi.frameshelf.entity.utility;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SortDto {
    private String field;
    private String direction;
}