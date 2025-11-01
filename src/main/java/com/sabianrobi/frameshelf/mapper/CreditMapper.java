package com.sabianrobi.frameshelf.mapper;

import com.sabianrobi.frameshelf.entity.movie.CastMember;
import com.sabianrobi.frameshelf.entity.movie.Credits;
import com.sabianrobi.frameshelf.entity.movie.CrewMember;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CreditMapper {
    public Credits mapToCredits(final Set<CastMember> castMembers, final Set<CrewMember> crewMembers) {
        return Credits.builder()
                .cast(castMembers)
                .crew(crewMembers)
                .build();
    }
}
