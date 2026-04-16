package com.douglasmarq.vamoscozinharapi.exception;

import java.time.Instant;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageResolver messages;

    public GlobalExceptionHandler(MessageResolver messages) {
        this.messages = messages;
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> handleApi(ApiException ex, HttpServletRequest req) {
        ApiError body =
                new ApiError(
                        Instant.now(),
                        ex.getStatus().value(),
                        ex.getStatus().getReasonPhrase(),
                        ex.getMessageKey(),
                        messages.resolve(ex.getMessageKey(), ex.getArgs()),
                        req.getRequestURI(),
                        null);
        return ResponseEntity.status(ex.getStatus()).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<ApiError.FieldViolation> violations =
                ex.getBindingResult().getFieldErrors().stream().map(this::toViolation).toList();
        ApiError body =
                new ApiError(
                        Instant.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        "error.validation",
                        messages.resolve("error.validation"),
                        req.getRequestURI(),
                        violations);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleResponseStatus(
            ResponseStatusException ex, HttpServletRequest req) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        String reason = ex.getReason();
        String resolved = reason == null ? status.getReasonPhrase() : messages.resolve(reason);
        ApiError body =
                new ApiError(
                        Instant.now(),
                        status.value(),
                        status.getReasonPhrase(),
                        reason,
                        resolved,
                        req.getRequestURI(),
                        null);
        return ResponseEntity.status(status).body(body);
    }

    private ApiError.FieldViolation toViolation(FieldError error) {
        String message = error.getDefaultMessage();
        String key = null;
        try {
            ConstraintViolation<?> cv = error.unwrap(ConstraintViolation.class);
            String template = cv.getMessageTemplate();
            if (template != null && template.startsWith("{") && template.endsWith("}")) {
                key = template.substring(1, template.length() - 1);
            }
        } catch (IllegalArgumentException ignored) {
        }
        return new ApiError.FieldViolation(error.getField(), key, message);
    }
}
