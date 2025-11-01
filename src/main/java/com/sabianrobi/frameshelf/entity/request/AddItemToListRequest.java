package com.sabianrobi.frameshelf.entity.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddItemToListRequest {
    @NotNull
    private Integer itemId;

    private String notes;
    private LocalDateTime watchedAt;
}
