package com.sabianrobi.frameshelf.entity.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateActorRequest {
    @NotNull
    int id;
}
