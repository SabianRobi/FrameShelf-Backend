package com.sabianrobi.frameshelf.entity.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchPeopleResponse {
    private boolean adult;
    private String gender;
    private int id;
    private String knownForDepartment;
    private String name;
    private String originalName;
    private double popularity;
    private String profilePath;
    private Set<KnownFor> knownFor;

    @Data
    @Builder
    public static class KnownFor {
        private boolean adult;
        private String backdropPath;
        private int id;
        private String title;
        private String originalLanguage;
        private String originalTitle;
        private String overview;
        private String posterPath;
        private String mediaType;
        //        private List<Integer> genreIds;
        private double popularity;
        private String releaseDate;
        private boolean video;
        private double voteAverage;
        private int voteCount;
    }
}
