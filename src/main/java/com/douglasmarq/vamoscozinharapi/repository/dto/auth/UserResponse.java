package com.douglasmarq.vamoscozinharapi.repository.dto.auth;

import java.time.LocalDateTime;

import com.douglasmarq.vamoscozinharapi.repository.entities.UserEntity;

public record UserResponse(Long id, String email, String name, LocalDateTime createdAt) {

    public static UserResponse of(UserEntity user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getName(), user.getCreatedAt());
    }
}
