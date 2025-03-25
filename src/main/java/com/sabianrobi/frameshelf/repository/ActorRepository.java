package com.sabianrobi.frameshelf.repository;

import com.sabianrobi.frameshelf.entity.Actor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer>, JpaSpecificationExecutor<Actor> {
    List<Actor> findAll(final Specification<Actor> specification);
}
