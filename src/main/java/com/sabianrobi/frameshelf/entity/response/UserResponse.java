package com.sabianrobi.frameshelf.entity.response;

import com.sabianrobi.frameshelf.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String username;
    private String displayName;
    private String profilePicture;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
    
    /**
     * Create a UserResponse from a User entity
     *
     * @param user The User entity
     * @return The UserResponse
     */
    public static UserResponse fromUser(User user) {
        if (user == null) {
            return null;
        }
        
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .profilePicture(user.getProfilePicture())
                .createdAt(user.getCreatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .build();
    }
}