package com.sabianrobi.frameshelf.mapper;

import com.sabianrobi.frameshelf.entity.person.MovieCastMember;
import com.sabianrobi.frameshelf.entity.person.MovieCredits;
import com.sabianrobi.frameshelf.entity.person.MovieCrewMember;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class MovieCreditMapper {

    public MovieCredits mapToMovieCredits(final Set<MovieCastMember> movieCastMembers, final Set<MovieCrewMember> movieCrewMembers) {
        return MovieCredits.builder()
                .cast(movieCastMembers)
                .crew(movieCrewMembers)
                .build();
    }
}
