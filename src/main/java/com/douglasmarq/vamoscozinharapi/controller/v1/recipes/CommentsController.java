package com.douglasmarq.vamoscozinharapi.controller.v1.recipes;

import jakarta.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.douglasmarq.vamoscozinharapi.annotation.RateLimit;
import com.douglasmarq.vamoscozinharapi.repository.dto.PageResponse;
import com.douglasmarq.vamoscozinharapi.repository.dto.comments.CommentRequest;
import com.douglasmarq.vamoscozinharapi.repository.dto.comments.CommentResponse;
import com.douglasmarq.vamoscozinharapi.security.AuthenticatedUser;
import com.douglasmarq.vamoscozinharapi.service.comments.CommentsService;

@RestController
@RequestMapping("/v1")
public class CommentsController {

    private static final int MAX_PAGE_SIZE = 50;

    private final CommentsService commentsService;

    public CommentsController(CommentsService commentsService) {
        this.commentsService = commentsService;
    }

    @GetMapping("/recipes/{recipeId}/comments")
    public ResponseEntity<PageResponse<CommentResponse>> list(
            @PathVariable Long recipeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = sanitize(page, size);
        return ResponseEntity.ok(commentsService.listByRecipe(recipeId, pageable));
    }

    @PostMapping("/recipes/{recipeId}/comments")
    @RateLimit(messageKey = "error.rateLimit.comment.create")
    public ResponseEntity<CommentResponse> create(
            @PathVariable Long recipeId,
            @AuthenticationPrincipal AuthenticatedUser principal,
            @Valid @RequestBody CommentRequest request) {
        CommentResponse created = commentsService.create(recipeId, principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/comments/{id}")
    public ResponseEntity<CommentResponse> update(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthenticatedUser principal,
            @Valid @RequestBody CommentRequest request) {
        return ResponseEntity.ok(commentsService.update(id, principal.getId(), request));
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id, @AuthenticationPrincipal AuthenticatedUser principal) {
        commentsService.delete(id, principal.getId());
        return ResponseEntity.noContent().build();
    }

    private static Pageable sanitize(int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        return PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}
