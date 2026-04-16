package com.douglasmarq.vamoscozinharapi.service.favorites.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.douglasmarq.vamoscozinharapi.repository.RecipeFavoriteRepository;
import com.douglasmarq.vamoscozinharapi.repository.dto.PageResponse;
import com.douglasmarq.vamoscozinharapi.repository.dto.recipes.FavoriteStatusResponse;
import com.douglasmarq.vamoscozinharapi.repository.dto.recipes.RecipeSummary;
import com.douglasmarq.vamoscozinharapi.repository.entities.RecipesEntity;
import com.douglasmarq.vamoscozinharapi.service.favorites.FavoritesService;

@Service
public class FavoritesServiceImpl implements FavoritesService {

    private final RecipeFavoriteRepository favoriteRepository;

    public FavoritesServiceImpl(RecipeFavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    @Transactional
    @Caching(
            evict = {
                @CacheEvict(value = "recipe", key = "#recipeId"),
                @CacheEvict(value = "recipes", allEntries = true),
                @CacheEvict(value = "recipesSearch", allEntries = true)
            })
    public FavoriteStatusResponse favorite(Long recipeId, Long userId) {
        if (!favoriteRepository.exists(userId, recipeId)) {
            favoriteRepository.save(userId, recipeId);
        }
        return new FavoriteStatusResponse(true, favoriteRepository.countByRecipe(recipeId));
    }

    @Override
    @Transactional
    @Caching(
            evict = {
                @CacheEvict(value = "recipe", key = "#recipeId"),
                @CacheEvict(value = "recipes", allEntries = true),
                @CacheEvict(value = "recipesSearch", allEntries = true)
            })
    public FavoriteStatusResponse unfavorite(Long recipeId, Long userId) {
        favoriteRepository.delete(userId, recipeId);
        return new FavoriteStatusResponse(false, favoriteRepository.countByRecipe(recipeId));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<RecipeSummary> listForUser(Long userId, Pageable pageable) {
        Page<RecipesEntity> page = favoriteRepository.findFavoriteRecipes(userId, pageable);
        Page<RecipeSummary> mapped =
                new PageImpl<>(
                        page.getContent().stream().map(RecipeSummary::of).toList(),
                        pageable,
                        page.getTotalElements());
        return PageResponse.of(mapped);
    }
}
