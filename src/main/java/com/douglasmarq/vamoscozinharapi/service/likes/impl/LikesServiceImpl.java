package com.douglasmarq.vamoscozinharapi.service.likes.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.douglasmarq.vamoscozinharapi.repository.RecipeLikeRepository;
import com.douglasmarq.vamoscozinharapi.repository.dto.recipes.LikeStatusResponse;
import com.douglasmarq.vamoscozinharapi.service.likes.LikesService;

@Service
public class LikesServiceImpl implements LikesService {

    private final RecipeLikeRepository likeRepository;

    public LikesServiceImpl(RecipeLikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    @Override
    @Transactional
    @Caching(
            evict = {
                @CacheEvict(value = "recipe", key = "#recipeId"),
                @CacheEvict(value = "recipes", allEntries = true),
                @CacheEvict(value = "recipesSearch", allEntries = true),
                @CacheEvict(value = "hotRecipesByLikes", allEntries = true)
            })
    public LikeStatusResponse like(Long recipeId, Long userId) {
        if (!likeRepository.exists(userId, recipeId)) {
            likeRepository.save(userId, recipeId);
        }
        return new LikeStatusResponse(true, likeRepository.countByRecipe(recipeId));
    }

    @Override
    @Transactional
    @Caching(
            evict = {
                @CacheEvict(value = "recipe", key = "#recipeId"),
                @CacheEvict(value = "recipes", allEntries = true),
                @CacheEvict(value = "recipesSearch", allEntries = true),
                @CacheEvict(value = "hotRecipesByLikes", allEntries = true)
            })
    public LikeStatusResponse unlike(Long recipeId, Long userId) {
        likeRepository.delete(userId, recipeId);
        return new LikeStatusResponse(false, likeRepository.countByRecipe(recipeId));
    }

    @Override
    @Transactional(readOnly = true)
    public LikeStatusResponse status(Long recipeId, Long userId) {
        Boolean liked = userId == null ? null : likeRepository.exists(userId, recipeId);
        return new LikeStatusResponse(liked, likeRepository.countByRecipe(recipeId));
    }
}
