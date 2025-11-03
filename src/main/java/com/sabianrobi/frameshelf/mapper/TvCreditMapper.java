package com.sabianrobi.frameshelf.mapper;

import com.sabianrobi.frameshelf.entity.person.TvCastMember;
import com.sabianrobi.frameshelf.entity.person.TvCredits;
import com.sabianrobi.frameshelf.entity.person.TvCrewMember;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TvCreditMapper {

    public TvCredits mapToTvCredits(final Set<TvCastMember> tvCastMembers, final Set<TvCrewMember> tvCrewMembers) {
        return TvCredits.builder()
                .cast(tvCastMembers)
                .crew(tvCrewMembers)
                .build();
    }
}
