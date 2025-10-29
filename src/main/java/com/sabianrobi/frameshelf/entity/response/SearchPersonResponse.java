package com.sabianrobi.frameshelf.entity.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchPersonResponse {
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

    private MovieCredits movieCredits;
    private TvCredits tvCredits;

    @Data
    @Builder
    public static class MovieCredits {
        private List<MovieCastMember> cast;
        private List<MovieCrewMember> crew;
    }

    @Data
    @Builder
    public static class TvCredits {
        private List<TvCastMember> cast;
        private List<TvCrewMember> crew;
    }

    @Data
    @Builder
    public static class MovieCastMember {
        private boolean adult;
        private String backdropPath;
        private String character;
        private String creditId;
        // private Set<Integer> genreIds;
        private int id;
        private int order;
        private String originalLanguage;
        private String originalTitle;
        private String overview;
        private double popularity;
        private String posterPath;
        private String releaseDate;
        private String title;
        private boolean video;
        private double voteAverage;
        private int voteCount;
    }

    @Data
    @Builder
    public static class MovieCrewMember {
        private boolean adult;
        private String backdropPath;
        private String creditId;
        private String department;
        // private Set<Integer> genreIds;
        private int id;
        private String job;
        private String originalLanguage;
        private String originalTitle;
        private String overview;
        private double popularity;
        private String posterPath;
        private String releaseDate;
        private String title;
        private boolean video;
        private double voteAverage;
        private int voteCount;
    }

    @Data
    @Builder
    public static class TvCastMember {
        private boolean adult;
        private String backdropPath;
        private String character;
        private String creditId;
        private int episodeCount;
        private String firstAirDate;
        private String firstCreditAirDate;
        // private Set<Integer> genreIds;
        private int id;
        private String name;
        private String originalLanguage;
        private String originalName;
        private List<String> originCountry;
        private String overview;
        private double popularity;
        private String posterPath;
        private double voteAverage;
        private int voteCount;
    }

    @Data
    @Builder
    public static class TvCrewMember {
        private boolean adult;
        private String backdropPath;
        private String creditId;
        private String department;
        private int episodeCount;
        private String firstAirDate;
        private String firstCreditAirDate;
        // private Set<Integer> genreIds;
        private int id;
        private String job;
        private String name;
        private String originalLanguage;
        private String originalName;
        private List<String> originCountry;
        private String overview;
        private double popularity;
        private String posterPath;
        private double voteAverage;
        private int voteCount;
    }
}
