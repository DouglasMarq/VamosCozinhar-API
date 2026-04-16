package com.douglasmarq.vamoscozinharapi.service.favorites;

import org.springframework.data.domain.Pageable;

import com.douglasmarq.vamoscozinharapi.repository.dto.PageResponse;
import com.douglasmarq.vamoscozinharapi.repository.dto.recipes.FavoriteStatusResponse;
import com.douglasmarq.vamoscozinharapi.repository.dto.recipes.RecipeSummary;

public interface FavoritesService {
    FavoriteStatusResponse favorite(Long recipeId, Long userId);

    FavoriteStatusResponse unfavorite(Long recipeId, Long userId);

    PageResponse<RecipeSummary> listForUser(Long userId, Pageable pageable);
}
