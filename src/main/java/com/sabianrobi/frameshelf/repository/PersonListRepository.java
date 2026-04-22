package com.sabianrobi.frameshelf.repository;

import com.sabianrobi.frameshelf.entity.PersonList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PersonListRepository extends CrudRepository<PersonList, UUID> {
    Page<PersonList> findByUserId(final UUID userId, final Pageable pageable);

    Page<PersonList> findByUserIdAndNameContainingIgnoreCase(final UUID userId, final String name, final Pageable pageable);
}
