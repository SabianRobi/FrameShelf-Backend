package com.sabianrobi.frameshelf.entity.request;

import com.sabianrobi.frameshelf.entity.ListType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateListRequest {
    @NotBlank
    private String name;

    @NotNull
    private ListType type;
}
