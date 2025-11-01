package com.sabianrobi.frameshelf.repository;

import com.sabianrobi.frameshelf.entity.movie.CastMember;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CastMemberRepository extends CrudRepository<CastMember, String> {
}
