package com.sabianrobi.frameshelf.entity;

import com.sabianrobi.frameshelf.entity.person.MovieCredits;
import com.sabianrobi.frameshelf.entity.person.TvCredits;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private boolean adult;

    @ElementCollection
    private List<String> alsoKnownAs;

    @Size(max = 10_000)
    private String biography;

    private String birthday;
    private String deathday;

    @Builder.Default
    private int gender = 0;

    private String homepage;

    @Id
    @Builder.Default
    private int id = 0;

    private String imdbId;
    private String knownForDepartment;
    private String name;
    private String placeOfBirth;
    private double popularity;
    private String profilePath;

    @OneToOne
    private MovieCredits movieCredits;

    @OneToOne
    private TvCredits tvCredits;
}
