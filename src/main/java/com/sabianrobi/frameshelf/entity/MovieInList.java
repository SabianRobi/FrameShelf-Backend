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
public class MovieInList extends BaseEntity {

    @ManyToOne
    private Movie movie;

    @ManyToOne
    private MovieList list;

    private LocalDateTime addedAt;
    private String notes;
    private LocalDateTime watchedAt;
    private Language watchedLanguage;
}
