package com.sabianrobi.frameshelf.repository;

import com.sabianrobi.frameshelf.entity.person.TvCrewMember;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TvCrewMemberRepository extends CrudRepository<TvCrewMember, String> {
}
