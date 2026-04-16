package com.douglasmarq.vamoscozinharapi.repository.dto.comments;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        @NotBlank(message = "{validation.comment.content.required}") @Size(min = 1, max = 1024, message = "{validation.comment.content.size}") String content) {}
