package com.douglasmarq.vamoscozinharapi.service.comments;

import org.springframework.data.domain.Pageable;

import com.douglasmarq.vamoscozinharapi.repository.dto.PageResponse;
import com.douglasmarq.vamoscozinharapi.repository.dto.comments.CommentRequest;
import com.douglasmarq.vamoscozinharapi.repository.dto.comments.CommentResponse;

public interface CommentsService {
    PageResponse<CommentResponse> listByRecipe(Long recipeId, Pageable pageable);

    CommentResponse create(Long recipeId, Long userId, CommentRequest request);

    CommentResponse update(Long commentId, Long userId, CommentRequest request);

    void delete(Long commentId, Long userId);
}
