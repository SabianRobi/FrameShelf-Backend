package com.sabianrobi.frameshelf.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditItemInListRequest {
    private String notes;
    private LocalDateTime watchedAt;
}
