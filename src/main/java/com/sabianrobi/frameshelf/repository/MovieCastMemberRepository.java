package com.sabianrobi.frameshelf.repository;

import com.sabianrobi.frameshelf.entity.person.MovieCastMember;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieCastMemberRepository extends CrudRepository<MovieCastMember, String> {
}
