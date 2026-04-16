package com.douglasmarq.vamoscozinharapi.service.auth.impl;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.douglasmarq.vamoscozinharapi.exception.ApiException;
import com.douglasmarq.vamoscozinharapi.repository.UserRepository;
import com.douglasmarq.vamoscozinharapi.repository.dto.auth.AuthResponse;
import com.douglasmarq.vamoscozinharapi.repository.dto.auth.LoginRequest;
import com.douglasmarq.vamoscozinharapi.repository.dto.auth.RegisterRequest;
import com.douglasmarq.vamoscozinharapi.repository.dto.auth.UserResponse;
import com.douglasmarq.vamoscozinharapi.repository.entities.UserEntity;
import com.douglasmarq.vamoscozinharapi.security.JwtService;
import com.douglasmarq.vamoscozinharapi.service.auth.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new ApiException(HttpStatus.CONFLICT, "error.auth.email.alreadyRegistered");
        }
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setName(request.name().trim());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        return UserResponse.of(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email().trim().toLowerCase(), request.password()));
        } catch (BadCredentialsException e) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "error.auth.invalidCredentials");
        }
        UserEntity user =
                userRepository
                        .findByEmail(request.email().trim().toLowerCase())
                        .orElseThrow(
                                () ->
                                        new ApiException(
                                                HttpStatus.UNAUTHORIZED,
                                                "error.auth.invalidCredentials"));
        String token = jwtService.issueToken(user);
        return new AuthResponse(token, jwtService.getExpirationSeconds(), UserResponse.of(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse me(Long userId) {
        UserEntity user =
                userRepository
                        .findById(userId)
                        .orElseThrow(
                                () ->
                                        new ApiException(
                                                HttpStatus.NOT_FOUND, "error.auth.userNotFound"));
        return UserResponse.of(user);
    }
}
