package com.sabianrobi.frameshelf.entity.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ListResponse {
    private UUID id;
    private String name;
    private UUID userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

