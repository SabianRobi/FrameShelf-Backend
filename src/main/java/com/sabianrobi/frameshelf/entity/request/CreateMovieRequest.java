package com.sabianrobi.frameshelf.entity.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CreateMovieRequest {
    @NotNull
    int id;

    String watchedLanguage;
    Date watchedAt;
}
