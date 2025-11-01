package com.sabianrobi.frameshelf.repository;

import com.sabianrobi.frameshelf.entity.movie.CrewMember;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewMemberRepository extends CrudRepository<CrewMember, String> {
}