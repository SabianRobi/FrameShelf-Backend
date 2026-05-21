package com.sabianrobi.frameshelf.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class User extends BaseEntity {
    @Column(unique = true)
    private String username;

    private String displayName;

    @Column(length = 1536)
    private String profilePicture;
    private LocalDateTime lastLoginAt;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private GoogleUser googleUser;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<List> lists = new HashSet<>();
}
