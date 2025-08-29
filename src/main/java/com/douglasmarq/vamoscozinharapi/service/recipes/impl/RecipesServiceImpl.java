package com.douglasmarq.vamoscozinharapi.service.recipes.impl;

import static com.douglasmarq.vamoscozinharapi.utils.StringUtils.sanitizeString;
import static com.douglasmarq.vamoscozinharapi.utils.StringUtils.validateImageUrl;

import java.util.List;
import java.util.Objects;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.douglasmarq.vamoscozinharapi.repository.RecipesRepository;
import com.douglasmarq.vamoscozinharapi.repository.dto.IngredientsDTO;
import com.douglasmarq.vamoscozinharapi.repository.dto.RecipeDTO;
import com.douglasmarq.vamoscozinharapi.repository.entities.RecipesEntity;
import com.douglasmarq.vamoscozinharapi.service.recipes.RecipesService;

@Service
public class RecipesServiceImpl implements RecipesService {

    private final RecipesRepository repository;

    public RecipesServiceImpl(RecipesRepository repository) {
        this.repository = repository;
    }

    private IngredientsDTO mapIngredientsDTO(IngredientsDTO ingredient) {
        return IngredientsDTO.of(
                sanitizeString(ingredient.ingredient()),
                sanitizeString(ingredient.description()),
                validateImageUrl(ingredient.image()));
    }

    @Override
    @Cacheable(value = "recipes")
    public List<RecipesEntity> getAllRecipes() {
        return repository.getAllRecipes();
    }

    @Override
    @CacheEvict(value = "recipes", allEntries = true)
    public void createRecipe(RecipeDTO recipe) {
        try {
            RecipesEntity entity = new RecipesEntity();
            entity.setName(recipe.name());
            entity.setDescription(recipe.description());
            entity.setDifficulty(recipe.difficulty());
            entity.setImage(recipe.image());

            List<IngredientsDTO> ingredients =
                    recipe.ingredients().stream()
                            .filter(Objects::nonNull)
                            .map(this::mapIngredientsDTO)
                            .toList();

            entity.setRecipeIngredients(ingredients);
            repository.save(entity);
        } catch (Exception e) {
            // TODO - Treat exception
            throw new RuntimeException(e);
        }
    }

    @Override
    @Cacheable(value = "recipe", key = "#id")
    public RecipesEntity getRecipeById(Long id) {
        return repository.getRecipeById(id);
    }

    @Override
    @CacheEvict(
            value = {"recipe", "recipes"},
            key = "#id")
    public boolean deleteRecipeById(Long id) {
        return repository.deleteById(id);
    }
}
