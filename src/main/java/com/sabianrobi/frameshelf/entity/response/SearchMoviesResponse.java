package com.sabianrobi.frameshelf.entity.response;

import com.sabianrobi.frameshelf.entity.MovieBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class SearchMoviesResponse extends MovieBase {
}
