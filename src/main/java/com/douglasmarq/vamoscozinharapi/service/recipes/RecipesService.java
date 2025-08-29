package com.douglasmarq.vamoscozinharapi.service.recipes;

import java.util.List;

import com.douglasmarq.vamoscozinharapi.repository.dto.RecipeDTO;
import com.douglasmarq.vamoscozinharapi.repository.entities.RecipesEntity;

public interface RecipesService {
    List<RecipesEntity> getAllRecipes();

    void createRecipe(RecipeDTO recipe);

    RecipesEntity getRecipeById(Long id);

    boolean deleteRecipeById(Long id);
}
