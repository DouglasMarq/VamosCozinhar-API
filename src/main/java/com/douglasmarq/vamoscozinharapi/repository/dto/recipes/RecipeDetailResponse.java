package com.douglasmarq.vamoscozinharapi.repository.dto.recipes;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.douglasmarq.vamoscozinharapi.repository.dto.IngredientsDTO;
import com.douglasmarq.vamoscozinharapi.repository.dto.PageResponse;
import com.douglasmarq.vamoscozinharapi.repository.dto.comments.CommentResponse;

public record RecipeDetailResponse(
        Long id,
        String name,
        String description,
        Integer difficulty,
        String image,
        List<IngredientsDTO> recipeIngredients,
        List<String> prepare,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        long likesCount,
        long commentsCount,
        long favoritesCount,
        long viewsCount,
        Boolean likedByMe,
        Boolean favoritedByMe,
        PageResponse<CommentResponse> comments)
        implements Serializable {}
