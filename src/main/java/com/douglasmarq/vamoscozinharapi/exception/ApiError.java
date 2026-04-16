package com.douglasmarq.vamoscozinharapi.exception;

import java.time.Instant;
import java.util.List;

public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String messageKey,
        String message,
        String path,
        List<FieldViolation> violations) {

    public record FieldViolation(String field, String messageKey, String message) {}
}
