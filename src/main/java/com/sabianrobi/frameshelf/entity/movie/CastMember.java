package com.sabianrobi.frameshelf.entity.movie;

import jakarta.persistence.Column;
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
public class CastMember {
    private boolean adult;
    private int castId;

    @Id
    private String creditId;

    @Column(name = "`character`")
    private String character;

    private int gender;
    private int id; // TMDB person ID
    private String knownForDepartment;
    private String name;

    @Column(name = "`order`")
    private int order;

    private String originalName;
    private double popularity;
    private String profilePath;
}