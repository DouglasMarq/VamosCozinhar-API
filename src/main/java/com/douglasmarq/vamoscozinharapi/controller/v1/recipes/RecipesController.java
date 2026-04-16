package com.douglasmarq.vamoscozinharapi.controller.v1.recipes;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.douglasmarq.vamoscozinharapi.annotation.RateLimit;
import com.douglasmarq.vamoscozinharapi.exception.MessageResolver;
import com.douglasmarq.vamoscozinharapi.repository.dto.PageResponse;
import com.douglasmarq.vamoscozinharapi.repository.dto.RecipeDTO;
import com.douglasmarq.vamoscozinharapi.repository.dto.RecipeSearchRequest;
import com.douglasmarq.vamoscozinharapi.repository.dto.recipes.RecipeDetailResponse;
import com.douglasmarq.vamoscozinharapi.repository.dto.recipes.RecipeSummary;
import com.douglasmarq.vamoscozinharapi.security.AuthenticatedUser;
import com.douglasmarq.vamoscozinharapi.service.recipes.RecipesService;

@RestController
@RequestMapping("/v1/recipes")
public class RecipesController {

    private static final int MAX_PAGE_SIZE = 50;
    private static final int DEFAULT_PAGE_SIZE = 10;

    private final RecipesService recipesService;
    private final MessageResolver messages;

    public RecipesController(RecipesService recipesService, MessageResolver messages) {
        this.recipesService = recipesService;
        this.messages = messages;
    }

    @GetMapping()
    public ResponseEntity<List<RecipeSummary>> getAllRecipes() {
        return ResponseEntity.ok().body(recipesService.getAllRecipes());
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<RecipeSummary>> searchRecipes(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer difficulty,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {

        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        if (size <= 0) safeSize = DEFAULT_PAGE_SIZE;

        Pageable pageable = PageRequest.of(safePage, safeSize, parseSort(sort));
        RecipeSearchRequest filters = RecipeSearchRequest.of(id, q, difficulty);
        return ResponseEntity.ok(recipesService.searchRecipes(filters, pageable));
    }

    private static Sort parseSort(String sort) {
        if (sort == null || sort.isBlank()) return Sort.by(Sort.Direction.DESC, "id");
        String[] parts = sort.split(",", 2);
        String property = parts[0].trim();
        Sort.Direction dir =
                (parts.length > 1 && "asc".equalsIgnoreCase(parts[1].trim()))
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;
        String safeProperty =
                switch (property) {
                    case "name" -> "name";
                    case "difficulty" -> "difficulty";
                    case "createdAt" -> "created_at";
                    case "updatedAt" -> "updated_at";
                    case "id" -> "id";
                    default -> "id";
                };
        return Sort.by(dir, safeProperty);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDetailResponse> getRecipeById(
            @PathVariable Long id, @AuthenticationPrincipal AuthenticatedUser principal) {
        Long viewerId = principal == null ? null : principal.getId();
        RecipeDetailResponse recipe = recipesService.getRecipeDetail(id, viewerId);
        if (recipe == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(recipe);
    }

    @PostMapping()
    @RateLimit(messageKey = "error.rateLimit.recipe.create")
    public ResponseEntity<String> createRecipe(
            @AuthenticationPrincipal AuthenticatedUser principal,
            @Valid @RequestBody RecipeDTO recipe) {
        recipesService.createRecipe(recipe);
        return ResponseEntity.status(201).body(messages.resolve("success.recipe.created"));
    }

    @DeleteMapping("/{id}")
    @RateLimit(messageKey = "error.rateLimit.recipe.delete")
    public ResponseEntity<String> deleteRecipeById(
            @PathVariable Long id, @AuthenticationPrincipal AuthenticatedUser principal) {
        boolean deleted = recipesService.deleteRecipeById(id);
        if (deleted) {
            return ResponseEntity.ok(messages.resolve("success.recipe.deleted"));
        }
        return ResponseEntity.notFound().build();
    }
}
