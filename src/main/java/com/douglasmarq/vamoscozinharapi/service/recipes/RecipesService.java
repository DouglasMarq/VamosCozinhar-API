package com.douglasmarq.vamoscozinharapi.service.recipes;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.douglasmarq.vamoscozinharapi.repository.dto.PageResponse;
import com.douglasmarq.vamoscozinharapi.repository.dto.RecipeDTO;
import com.douglasmarq.vamoscozinharapi.repository.dto.RecipeSearchRequest;
import com.douglasmarq.vamoscozinharapi.repository.dto.recipes.RecipeDetailResponse;
import com.douglasmarq.vamoscozinharapi.repository.dto.recipes.RecipeSummary;
import com.douglasmarq.vamoscozinharapi.repository.entities.RecipesEntity;

public interface RecipesService {
    List<RecipeSummary> getAllRecipes();

    void createRecipe(RecipeDTO recipe);

    RecipesEntity getRecipeEntity(Long id);

    RecipeDetailResponse getRecipeDetail(Long id, Long viewerUserId);

    boolean deleteRecipeById(Long id);

    PageResponse<RecipeSummary> searchRecipes(RecipeSearchRequest filters, Pageable pageable);
}
