package com.sabianrobi.frameshelf.repository;

import com.sabianrobi.frameshelf.entity.PersonList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PersonListRepository extends CrudRepository<PersonList, UUID> {
    java.util.List<PersonList> findByUserId(final UUID userId);

    java.util.List<PersonList> findByUserIdAndNameContainingIgnoreCase(final UUID userId, final String name);
}
