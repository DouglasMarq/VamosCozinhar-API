package com.douglasmarq.vamoscozinharapi.controller.v1.recipes;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.douglasmarq.vamoscozinharapi.annotation.RateLimit;
import com.douglasmarq.vamoscozinharapi.repository.dto.RecipeDTO;
import com.douglasmarq.vamoscozinharapi.repository.entities.RecipesEntity;
import com.douglasmarq.vamoscozinharapi.service.recipes.RecipesService;

@RestController
@RequestMapping("/v1/recipes")
public class RecipesController {

    private final RecipesService recipesService;

    public RecipesController(RecipesService recipesService) {
        this.recipesService = recipesService;
    }

    @GetMapping()
    @Cacheable(value = "recipes")
    public ResponseEntity<List<RecipesEntity>> getAllRecipes() {
        return ResponseEntity.ok().body(recipesService.getAllRecipes());
    }

    @GetMapping("/{id}")
    @Cacheable(value = "recipe", key = "#id")
    public ResponseEntity<RecipesEntity> getRecipeById(@PathVariable Long id) {
        RecipesEntity recipe = recipesService.getRecipeById(id);
        if (recipe == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(recipe);
    }

    // TODO - Protect this route
    @PostMapping()
    @RateLimit(message = "Too many requests to create recipe. Please wait before trying again.")
    public ResponseEntity<String> createRecipe(@Valid @RequestBody RecipeDTO recipe) {
        recipesService.createRecipe(recipe);
        return ResponseEntity.status(201).body("Recipe created successfully");
    }

    // TODO - Protect this route
    @DeleteMapping("/{id}")
    @RateLimit(message = "Too many requests to create recipe. Please wait before trying again.")
    public ResponseEntity<String> deleteRecipeById(@PathVariable Long id) {
        boolean deleted = recipesService.deleteRecipeById(id);
        if (deleted) {
            return ResponseEntity.ok("Recipe deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }
}
