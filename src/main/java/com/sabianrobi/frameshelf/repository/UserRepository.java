package com.sabianrobi.frameshelf.repository;

import com.sabianrobi.frameshelf.entity.GoogleUser;
import com.sabianrobi.frameshelf.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
    Optional<User> findByGoogleUser(final GoogleUser googleUser);

    Optional<User> findByUsername(final String username);
}