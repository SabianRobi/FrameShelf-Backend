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
public class MovieCastMember {
    private boolean adult;
    private String backdropPath;

    @Column(name = "`character`")
    private String character;

    @Id
    private String creditId;

    @ElementCollection
    private List<Integer> genreIds;

    private int id; // TMDB person ID

    @Column(name = "`order`")
    private int order;

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
