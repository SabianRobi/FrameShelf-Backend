package com.sabianrobi.frameshelf.repository;

import com.sabianrobi.frameshelf.entity.SpokenLanguage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SpokenLanguageRepository extends CrudRepository<SpokenLanguage, String> {
    Set<SpokenLanguage> findAllByIso6391In(Set<String> iso6391s);
}
