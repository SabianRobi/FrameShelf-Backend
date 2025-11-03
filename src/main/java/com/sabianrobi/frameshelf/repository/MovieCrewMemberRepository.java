package com.sabianrobi.frameshelf.repository;

import com.sabianrobi.frameshelf.entity.person.MovieCrewMember;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieCrewMemberRepository extends CrudRepository<MovieCrewMember, String> {
}
