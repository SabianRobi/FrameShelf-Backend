package com.sabianrobi.frameshelf.entity.movie;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrewMember {
    private boolean adult;
    private String gender;

    private int id; // TMDB person ID
    private String knownForDepartment;
    private String name;
    private String originalName;
    private double popularity;
    private String profilePath;

    @Id
    private String creditId; // Use creditId as primary key (unique per role)

    private String department;
    private String job;
}
