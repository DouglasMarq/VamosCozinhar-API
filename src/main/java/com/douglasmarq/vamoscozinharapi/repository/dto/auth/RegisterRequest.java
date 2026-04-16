package com.douglasmarq.vamoscozinharapi.repository.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "{validation.email.required}") @Email(message = "{validation.email.invalid}") String email,
        @NotBlank(message = "{validation.name.required}") @Size(min = 1, max = 255, message = "{validation.name.size}") String name,
        @NotBlank(message = "{validation.password.required}") @Size(min = 8, max = 100, message = "{validation.password.size}") String password) {}
