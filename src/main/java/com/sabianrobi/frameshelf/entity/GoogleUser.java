package com.sabianrobi.frameshelf.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleUser {
    @Id
    private String googleId;

    @Column(unique = true)
    private String email;

    @Column(length = 1536)
    private String picture;

    private String fullName;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
}
