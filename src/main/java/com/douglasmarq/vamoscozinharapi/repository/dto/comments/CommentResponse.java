package com.douglasmarq.vamoscozinharapi.repository.dto.comments;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.douglasmarq.vamoscozinharapi.repository.entities.RecipeCommentEntity;

public record CommentResponse(
        Long id,
        Long recipeId,
        Long userId,
        String userName,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt)
        implements Serializable {

    public static CommentResponse of(RecipeCommentEntity entity, String userName) {
        return new CommentResponse(
                entity.getId(),
                entity.getRecipeId(),
                entity.getUserId(),
                userName,
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
