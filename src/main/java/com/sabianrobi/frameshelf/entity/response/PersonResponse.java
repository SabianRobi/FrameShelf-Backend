package com.sabianrobi.frameshelf.entity.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonResponse {
    private int id;
    private String name;
    private String birthday;
    private String profilePath;
}
