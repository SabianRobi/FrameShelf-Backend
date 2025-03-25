package com.sabianrobi.frameshelf.repository;

import com.sabianrobi.frameshelf.entity.Collection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionRepository extends CrudRepository<Collection, Integer> {
}
