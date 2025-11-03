package com.sabianrobi.frameshelf.repository;

import com.sabianrobi.frameshelf.entity.person.TvCastMember;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TvCastMemberRepository extends CrudRepository<TvCastMember, String> {
}
