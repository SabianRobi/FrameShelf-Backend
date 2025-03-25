package com.sabianrobi.frameshelf.repository;

import com.sabianrobi.frameshelf.entity.ProductionCountry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
public interface ProductionCountryRepository extends CrudRepository<ProductionCountry, String> {
    Set<ProductionCountry> findAllByIso31661In(final Collection<String> iso31661s);
}
