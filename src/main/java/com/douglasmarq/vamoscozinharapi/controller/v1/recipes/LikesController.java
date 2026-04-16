package com.douglasmarq.vamoscozinharapi.controller.v1.recipes;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.douglasmarq.vamoscozinharapi.repository.dto.recipes.LikeStatusResponse;
import com.douglasmarq.vamoscozinharapi.security.AuthenticatedUser;
import com.douglasmarq.vamoscozinharapi.service.likes.LikesService;

@RestController
@RequestMapping("/v1/recipes/{recipeId}/like")
public class LikesController {

    private final LikesService likesService;

    public LikesController(LikesService likesService) {
        this.likesService = likesService;
    }

    @GetMapping
    public ResponseEntity<LikeStatusResponse> status(
            @PathVariable Long recipeId, @AuthenticationPrincipal AuthenticatedUser principal) {
        Long userId = principal == null ? null : principal.getId();
        return ResponseEntity.ok(likesService.status(recipeId, userId));
    }

    @PostMapping
    public ResponseEntity<LikeStatusResponse> like(
            @PathVariable Long recipeId, @AuthenticationPrincipal AuthenticatedUser principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(likesService.like(recipeId, principal.getId()));
    }

    @DeleteMapping
    public ResponseEntity<LikeStatusResponse> unlike(
            @PathVariable Long recipeId, @AuthenticationPrincipal AuthenticatedUser principal) {
        return ResponseEntity.ok(likesService.unlike(recipeId, principal.getId()));
    }
}
