package com.sabianrobi.frameshelf.entity.response;

import com.sabianrobi.frameshelf.entity.Movie;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class MovieResponse extends Movie {
}
