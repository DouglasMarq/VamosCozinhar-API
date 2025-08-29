package com.douglasmarq.vamoscozinharapi.controller.v1.recipes;

import java.util.List;

import com.douglasmarq.vamoscozinharapi.annotation.RateLimit;
import jakarta.validation.Valid;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.douglasmarq.vamoscozinharapi.repository.dto.RateRecipeDTO;
import com.douglasmarq.vamoscozinharapi.repository.entities.HotRecipesEntity;
import com.douglasmarq.vamoscozinharapi.service.recipes.HotRecipesService;

@RestController
@RequestMapping("/v1/hotrecipes")
public class HotRecipesController {
    private final HotRecipesService hotRecipesService;

    public HotRecipesController(HotRecipesService hotRecipesService) {
        this.hotRecipesService = hotRecipesService;
    }

    @GetMapping("/hot/views")
    @Cacheable(value = "hotRecipesByViews", unless = "#result.body.isEmpty()")
    public ResponseEntity<List<HotRecipesEntity>> getHotRecipesByViews() {
        return ResponseEntity.ok(hotRecipesService.getHotRecipesByViews());
    }

    @GetMapping("/hot/likes")
    @Cacheable(value = "hotRecipesByLikes", unless = "#result.body.isEmpty()")
    public ResponseEntity<List<HotRecipesEntity>> getHotRecipesByLikes() {
        return ResponseEntity.ok(hotRecipesService.getHotRecipesByLikes());
    }

    // TODO - Protect this route
    @PatchMapping("/hot/{id}")
    @RateLimit(message = "Too many requests to create recipe. Please wait before trying again.")
    public ResponseEntity<Boolean> patchHotRecipe(
            @PathVariable Long id, @Valid @RequestBody RateRecipeDTO payload) {
        return ResponseEntity.ok(hotRecipesService.rateHotRecipe(id, payload));
    }
}
