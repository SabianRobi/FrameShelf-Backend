package com.sabianrobi.frameshelf.entity.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateListRequest {
    @NotBlank
    private String name;

    @NotNull
    private String type; // "MOVIE" or "ACTOR"
}
