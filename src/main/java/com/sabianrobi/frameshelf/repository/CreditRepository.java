package com.sabianrobi.frameshelf.repository;

import com.sabianrobi.frameshelf.entity.movie.Credits;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CreditRepository extends CrudRepository<Credits, UUID> {
}
