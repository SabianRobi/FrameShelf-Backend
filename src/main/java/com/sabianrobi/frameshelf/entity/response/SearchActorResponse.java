package com.sabianrobi.frameshelf.entity.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchActorResponse {
    private int id;
    private String name;
    private String profile_path;
}
