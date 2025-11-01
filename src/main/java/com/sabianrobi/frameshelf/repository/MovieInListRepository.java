package com.sabianrobi.frameshelf.repository;

import com.sabianrobi.frameshelf.entity.MovieInList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MovieInListRepository extends CrudRepository<MovieInList, UUID> {
}
