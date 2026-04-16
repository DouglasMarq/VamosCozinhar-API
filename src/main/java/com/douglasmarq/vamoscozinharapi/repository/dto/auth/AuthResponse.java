package com.douglasmarq.vamoscozinharapi.repository.dto.auth;

public record AuthResponse(String token, long expiresInSeconds, UserResponse user) {}
