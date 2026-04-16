package com.douglasmarq.vamoscozinharapi.repository.dto.recipes;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.douglasmarq.vamoscozinharapi.repository.entities.RecipesEntity;

public record RecipeSummary(
        Long id,
        String name,
        String description,
        Integer difficulty,
        String image,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        long likesCount,
        long commentsCount,
        long favoritesCount,
        long viewsCount)
        implements Serializable {

    public static RecipeSummary of(RecipesEntity r) {
        return new RecipeSummary(
                r.getId(),
                r.getName(),
                r.getDescription(),
                r.getDifficulty(),
                r.getImage(),
                r.getCreatedAt(),
                r.getUpdatedAt(),
                nz(r.getLikesCount()),
                nz(r.getCommentsCount()),
                nz(r.getFavoritesCount()),
                nz(r.getViewsCount()));
    }

    private static long nz(Long v) {
        return v == null ? 0L : v;
    }
}
