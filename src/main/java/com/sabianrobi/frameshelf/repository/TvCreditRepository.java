package com.sabianrobi.frameshelf.repository;

import com.sabianrobi.frameshelf.entity.person.TvCredits;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TvCreditRepository extends CrudRepository<TvCredits, UUID> {
}
