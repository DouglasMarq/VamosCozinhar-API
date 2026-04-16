package com.douglasmarq.vamoscozinharapi.service.auth;

import com.douglasmarq.vamoscozinharapi.repository.dto.auth.AuthResponse;
import com.douglasmarq.vamoscozinharapi.repository.dto.auth.LoginRequest;
import com.douglasmarq.vamoscozinharapi.repository.dto.auth.RegisterRequest;
import com.douglasmarq.vamoscozinharapi.repository.dto.auth.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    UserResponse me(Long userId);
}
