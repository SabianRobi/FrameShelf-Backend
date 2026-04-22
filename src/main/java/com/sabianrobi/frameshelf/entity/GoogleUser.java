package com.sabianrobi.frameshelf.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GoogleUser extends BaseEntity {
    private String googleId;

    @Column(unique = true)
    private String email;

    @Column(length = 1536)
    private String picture;

    private String fullName;
    private LocalDateTime lastLoginAt;
}
