package com.sabianrobi.frameshelf.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String username;

    private String displayName;

    @Column(length = 1536)
    private String profilePicture;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;

    @OneToOne
    private GoogleUser googleUser;

    @OneToMany
    private Set<Movie> likedMovies;

    @OneToMany
    private Set<Actor> followedActors;
}