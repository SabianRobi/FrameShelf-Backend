package com.sabianrobi.frameshelf.repository;

import com.sabianrobi.frameshelf.entity.movie.ProductionCompany;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProductionCompanyRepository extends CrudRepository<ProductionCompany, Integer> {
    Set<ProductionCompany> findAllByIdIn(final Set<Integer> ids);
}
