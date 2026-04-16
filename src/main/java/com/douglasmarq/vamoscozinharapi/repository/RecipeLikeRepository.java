package com.douglasmarq.vamoscozinharapi.repository;

import org.springframework.stereotype.Repository;

import com.douglasmarq.vamoscozinharapi.repository.entities.RecipeLikeEntity;

@Repository
public class RecipeLikeRepository {

    private final IRecipeLikeRepository repository;

    public RecipeLikeRepository(IRecipeLikeRepository repository) {
        this.repository = repository;
    }

    public boolean exists(Long userId, Long recipeId) {
        return repository.existsByUserAndRecipe(userId, recipeId);
    }

    public long countByRecipe(Long recipeId) {
        return repository.countByRecipe(recipeId);
    }

    public RecipeLikeEntity save(Long userId, Long recipeId) {
        RecipeLikeEntity entity = new RecipeLikeEntity();
        entity.setUserId(userId);
        entity.setRecipeId(recipeId);
        return repository.save(entity);
    }

    public boolean delete(Long userId, Long recipeId) {
        return repository.deleteByUserAndRecipe(userId, recipeId) > 0;
    }
}
