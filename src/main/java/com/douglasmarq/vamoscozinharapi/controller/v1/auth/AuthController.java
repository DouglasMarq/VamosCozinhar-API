package com.douglasmarq.vamoscozinharapi.controller.v1.auth;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.douglasmarq.vamoscozinharapi.repository.dto.auth.AuthResponse;
import com.douglasmarq.vamoscozinharapi.repository.dto.auth.LoginRequest;
import com.douglasmarq.vamoscozinharapi.repository.dto.auth.RegisterRequest;
import com.douglasmarq.vamoscozinharapi.repository.dto.auth.UserResponse;
import com.douglasmarq.vamoscozinharapi.security.AuthenticatedUser;
import com.douglasmarq.vamoscozinharapi.service.auth.AuthService;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(@AuthenticationPrincipal AuthenticatedUser principal) {
        return ResponseEntity.ok(authService.me(principal.getId()));
    }
}
