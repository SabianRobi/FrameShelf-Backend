package com.sabianrobi.frameshelf.repository;

import com.sabianrobi.frameshelf.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer>, JpaSpecificationExecutor<Person> {
//    List<Actor> findAll(final Specification<Actor> specification);
}
