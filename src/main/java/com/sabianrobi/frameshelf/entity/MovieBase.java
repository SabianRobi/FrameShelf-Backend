package com.sabianrobi.frameshelf.entity;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class MovieBase {
    @Id
    int id;

    @Size(max = 1000)
    String overview;

    String title;
    String releaseDate;
    String originalTitle;
    String posterPath;
}
