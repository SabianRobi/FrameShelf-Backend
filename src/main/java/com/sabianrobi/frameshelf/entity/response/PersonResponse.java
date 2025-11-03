package com.sabianrobi.frameshelf.entity.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PersonResponse {
    private boolean adult;
    private List<String> alsoKnownAs;
    private String biography;
    private String birthday;
    private String deathday;
    private int gender;
    private String homepage;
    private int id;
    private String imdbId;
    private String knownForDepartment;
    private String name;
    private String placeOfBirth;
    private double popularity;
    private String profilePath;
//    private MovieCreditsResponse movieCredits;
//    private TvCreditsResponse tvCredits;
}
