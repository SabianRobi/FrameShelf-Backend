package com.sabianrobi.frameshelf.entity.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActorResponse {
    private int id;
    private String name;
    private String birthday;
    private String profile_path;
}
