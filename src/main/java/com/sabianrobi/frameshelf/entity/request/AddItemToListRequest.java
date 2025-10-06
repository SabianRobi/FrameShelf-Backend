package com.sabianrobi.frameshelf.entity.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddItemToListRequest {
    @NotNull
    private Integer itemId;
}
