package com.douglasmarq.vamoscozinharapi.controller.v1.recipes;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<List<HotRecipesEntity>> getHotRecipesByViews() {
        return ResponseEntity.ok(hotRecipesService.getHotRecipesByViews());
    }

    @GetMapping("/hot/likes")
    public ResponseEntity<List<HotRecipesEntity>> getHotRecipesByLikes() {
        return ResponseEntity.ok(hotRecipesService.getHotRecipesByLikes());
    }
}
