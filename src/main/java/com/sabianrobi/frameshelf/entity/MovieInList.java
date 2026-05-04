package com.sabianrobi.frameshelf.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MovieInList extends ItemInList {
    @ManyToOne
    private Movie movie;

    @ManyToOne
    private MovieList list;

    private LocalDateTime watchedAt;
    private Language watchedLanguage;
}
