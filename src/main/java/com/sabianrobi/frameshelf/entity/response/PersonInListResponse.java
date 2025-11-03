package com.sabianrobi.frameshelf.entity.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PersonInListResponse {
    private UUID id;
    private PersonResponse person;
    private LocalDateTime addedAt;
    private String notes;
}
