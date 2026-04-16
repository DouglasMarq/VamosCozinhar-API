package com.douglasmarq.vamoscozinharapi.controller.v1.recipes;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.douglasmarq.vamoscozinharapi.repository.dto.recipes.FavoriteStatusResponse;
import com.douglasmarq.vamoscozinharapi.security.AuthenticatedUser;
import com.douglasmarq.vamoscozinharapi.service.favorites.FavoritesService;

@RestController
@RequestMapping("/v1/recipes/{recipeId}/favorite")
public class FavoritesController {

    private final FavoritesService favoritesService;

    public FavoritesController(FavoritesService favoritesService) {
        this.favoritesService = favoritesService;
    }

    @PostMapping
    public ResponseEntity<FavoriteStatusResponse> favorite(
            @PathVariable Long recipeId, @AuthenticationPrincipal AuthenticatedUser principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(favoritesService.favorite(recipeId, principal.getId()));
    }

    @DeleteMapping
    public ResponseEntity<FavoriteStatusResponse> unfavorite(
            @PathVariable Long recipeId, @AuthenticationPrincipal AuthenticatedUser principal) {
        return ResponseEntity.ok(favoritesService.unfavorite(recipeId, principal.getId()));
    }
}
