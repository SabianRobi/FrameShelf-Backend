package com.sabianrobi.frameshelf.entity.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class MovieInListResponse {
    private UUID id;
    private MovieWithoutCreditsResponse movie;
    private LocalDateTime addedAt;
    private String notes;
    private LocalDateTime watchedAt;
}
