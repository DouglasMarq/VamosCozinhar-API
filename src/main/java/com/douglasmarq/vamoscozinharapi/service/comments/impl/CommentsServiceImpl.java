package com.douglasmarq.vamoscozinharapi.service.comments.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.douglasmarq.vamoscozinharapi.exception.ApiException;
import com.douglasmarq.vamoscozinharapi.repository.IUserRepository;
import com.douglasmarq.vamoscozinharapi.repository.RecipeCommentRepository;
import com.douglasmarq.vamoscozinharapi.repository.UserRepository;
import com.douglasmarq.vamoscozinharapi.repository.dto.PageResponse;
import com.douglasmarq.vamoscozinharapi.repository.dto.comments.CommentRequest;
import com.douglasmarq.vamoscozinharapi.repository.dto.comments.CommentResponse;
import com.douglasmarq.vamoscozinharapi.repository.entities.RecipeCommentEntity;
import com.douglasmarq.vamoscozinharapi.repository.entities.UserEntity;
import com.douglasmarq.vamoscozinharapi.service.comments.CommentsService;

@Service
public class CommentsServiceImpl implements CommentsService {

    private final RecipeCommentRepository commentRepository;
    private final UserRepository userRepository;
    private final IUserRepository userJpaRepository;

    public CommentsServiceImpl(
            RecipeCommentRepository commentRepository,
            UserRepository userRepository,
            IUserRepository userJpaRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CommentResponse> listByRecipe(Long recipeId, Pageable pageable) {
        Page<RecipeCommentEntity> page = commentRepository.findByRecipe(recipeId, pageable);
        List<Long> userIds =
                page.getContent().stream().map(RecipeCommentEntity::getUserId).toList();
        Map<Long, String> namesById =
                userJpaRepository.findAllById(userIds).stream()
                        .collect(Collectors.toMap(UserEntity::getId, UserEntity::getName));
        Page<CommentResponse> mapped =
                new PageImpl<>(
                        page.getContent().stream()
                                .map(c -> CommentResponse.of(c, namesById.get(c.getUserId())))
                                .toList(),
                        pageable,
                        page.getTotalElements());
        return PageResponse.of(mapped);
    }

    @Override
    @Transactional
    public CommentResponse create(Long recipeId, Long userId, CommentRequest request) {
        UserEntity user = requireUser(userId);
        RecipeCommentEntity entity = new RecipeCommentEntity();
        entity.setRecipeId(recipeId);
        entity.setUserId(userId);
        entity.setContent(request.content().trim());
        return CommentResponse.of(commentRepository.save(entity), user.getName());
    }

    @Override
    @Transactional
    public CommentResponse update(Long commentId, Long userId, CommentRequest request) {
        RecipeCommentEntity entity = requireComment(commentId);
        if (!entity.getUserId().equals(userId)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "error.comment.notOwner");
        }
        entity.setContent(request.content().trim());
        UserEntity user = requireUser(userId);
        return CommentResponse.of(commentRepository.save(entity), user.getName());
    }

    @Override
    @Transactional
    public void delete(Long commentId, Long userId) {
        RecipeCommentEntity entity = requireComment(commentId);
        if (!entity.getUserId().equals(userId)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "error.comment.notOwner");
        }
        commentRepository.deleteById(commentId);
    }

    private RecipeCommentEntity requireComment(Long id) {
        return commentRepository
                .findById(id)
                .orElseThrow(
                        () -> new ApiException(HttpStatus.NOT_FOUND, "error.comment.notFound"));
    }

    private UserEntity requireUser(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(
                        () -> new ApiException(HttpStatus.NOT_FOUND, "error.auth.userNotFound"));
    }

    @SuppressWarnings("unused")
    private static <T, K> Map<K, T> indexBy(List<T> items, Function<T, K> keyFn) {
        return items.stream().collect(Collectors.toMap(keyFn, Function.identity()));
    }
}
