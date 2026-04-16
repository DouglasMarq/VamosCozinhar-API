package com.douglasmarq.vamoscozinharapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.douglasmarq.vamoscozinharapi.repository.entities.RecipeFavoriteEntity;
import com.douglasmarq.vamoscozinharapi.repository.entities.RecipesEntity;

@Repository
public class RecipeFavoriteRepository {

    private final IRecipeFavoriteRepository repository;

    public RecipeFavoriteRepository(IRecipeFavoriteRepository repository) {
        this.repository = repository;
    }

    public boolean exists(Long userId, Long recipeId) {
        return repository.existsByUserAndRecipe(userId, recipeId);
    }

    public long countByRecipe(Long recipeId) {
        return repository.countByRecipe(recipeId);
    }

    public RecipeFavoriteEntity save(Long userId, Long recipeId) {
        RecipeFavoriteEntity entity = new RecipeFavoriteEntity();
        entity.setUserId(userId);
        entity.setRecipeId(recipeId);
        return repository.save(entity);
    }

    public boolean delete(Long userId, Long recipeId) {
        return repository.deleteByUserAndRecipe(userId, recipeId) > 0;
    }

    public Page<RecipesEntity> findFavoriteRecipes(Long userId, Pageable pageable) {
        return repository.findFavoriteRecipes(userId, pageable);
    }
}
