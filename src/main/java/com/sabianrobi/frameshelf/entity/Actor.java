package com.sabianrobi.frameshelf.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Actor {
    // More important fields
    @Id
    private int id;

    private String name;
    private String birthday;
    private String profilePath;


    // Less important fields
    @Size(max = 10_000)
    private String biography;

    private boolean adult;
    //    private List<String> alsoKnownAs;
    private String deathday;
    private int gender;
    private String homepage;
    private String imdbId;
    private String knownForDepartment;
    private String placeOfBirth;
    private double popularity;
}
