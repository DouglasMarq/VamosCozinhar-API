package com.douglasmarq.vamoscozinharapi.service.likes;

import com.douglasmarq.vamoscozinharapi.repository.dto.recipes.LikeStatusResponse;

public interface LikesService {
    LikeStatusResponse like(Long recipeId, Long userId);

    LikeStatusResponse unlike(Long recipeId, Long userId);

    LikeStatusResponse status(Long recipeId, Long userId);
}
