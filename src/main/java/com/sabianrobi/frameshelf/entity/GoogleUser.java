package com.sabianrobi.frameshelf.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(length = 1024)
    private String picture;
    
    private String fullName;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime lastLoginAt;
}