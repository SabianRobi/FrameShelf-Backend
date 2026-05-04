package com.sabianrobi.frameshelf.entity.response;

import com.sabianrobi.frameshelf.entity.Language;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class MovieInListResponse extends ItemInListResponse {
    private MovieWithoutCreditsResponse movie;
    private LocalDateTime watchedAt;
    private Language watchedLanguage;
}
