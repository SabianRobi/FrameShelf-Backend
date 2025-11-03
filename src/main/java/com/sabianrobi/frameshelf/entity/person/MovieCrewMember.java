package com.sabianrobi.frameshelf.entity.person;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class MovieCrewMember {
    private boolean adult;
    private String backdropPath;

    @Id
    private String creditId;

    private String department;

    @ElementCollection
    private List<Integer> genreIds;

    private int id; // TMDB person ID
    private String job;
    private String originalLanguage;
    private String originalTitle;

    @Column(length = 1000)
    private String overview;

    private double popularity;
    private String posterPath;
    private String releaseDate;
    private String title;
    private boolean video;
    private double voteAverage;
    private int voteCount;
}
