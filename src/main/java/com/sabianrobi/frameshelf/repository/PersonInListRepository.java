package com.sabianrobi.frameshelf.repository;

import com.sabianrobi.frameshelf.entity.PersonInList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PersonInListRepository extends CrudRepository<PersonInList, UUID> {
    Page<PersonInList> findByListId(final UUID listId, final Pageable pageable);
}
