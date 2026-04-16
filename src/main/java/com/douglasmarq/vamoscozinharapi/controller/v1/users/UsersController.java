package com.douglasmarq.vamoscozinharapi.controller.v1.users;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.douglasmarq.vamoscozinharapi.repository.dto.PageResponse;
import com.douglasmarq.vamoscozinharapi.repository.dto.recipes.RecipeSummary;
import com.douglasmarq.vamoscozinharapi.security.AuthenticatedUser;
import com.douglasmarq.vamoscozinharapi.service.favorites.FavoritesService;

@RestController
@RequestMapping("/v1/users/me")
public class UsersController {

    private static final int MAX_PAGE_SIZE = 50;

    private final FavoritesService favoritesService;

    public UsersController(FavoritesService favoritesService) {
        this.favoritesService = favoritesService;
    }

    @GetMapping("/favorites")
    public ResponseEntity<PageResponse<RecipeSummary>> favorites(
            @AuthenticationPrincipal AuthenticatedUser principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, "id"));
        return ResponseEntity.ok(favoritesService.listForUser(principal.getId(), pageable));
    }
}
