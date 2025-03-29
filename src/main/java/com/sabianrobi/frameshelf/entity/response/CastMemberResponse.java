package com.sabianrobi.frameshelf.entity.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CastMemberResponse {
    private Integer id;
    private String name;
    private String originalName;
    private String profilePath;
    private String character;
    //    private Boolean adult;
    //    private Integer gender;
    //    private String knownForDepartment;
    //    private Double popularity;
    //    private Integer castId;
    //    private Integer creditId;
    //    private Integer order;
}
