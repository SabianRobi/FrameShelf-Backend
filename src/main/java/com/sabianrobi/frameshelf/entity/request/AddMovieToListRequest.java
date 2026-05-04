package com.sabianrobi.frameshelf.entity.request;

import com.sabianrobi.frameshelf.entity.Language;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class AddMovieToListRequest extends AddItemToListRequest {
    private LocalDateTime watchedAt;
    private Language watchedLanguage;
}
