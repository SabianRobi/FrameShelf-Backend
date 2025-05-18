package com.sabianrobi.frameshelf.repository;

import com.sabianrobi.frameshelf.entity.GoogleUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoogleUserRepository extends CrudRepository<GoogleUser, Integer> {

    Optional<GoogleUser> findByGoogleId(final String googleId);
}