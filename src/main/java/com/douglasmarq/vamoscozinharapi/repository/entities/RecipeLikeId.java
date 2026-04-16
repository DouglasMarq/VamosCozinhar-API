package com.douglasmarq.vamoscozinharapi.repository.entities;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class RecipeLikeId implements Serializable {
    @Serial private static final long serialVersionUID = 1L;

    private Long userId;
    private Long recipeId;

    public RecipeLikeId() {}

    public RecipeLikeId(Long userId, Long recipeId) {
        this.userId = userId;
        this.recipeId = recipeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeLikeId that)) return false;
        return Objects.equals(userId, that.userId) && Objects.equals(recipeId, that.recipeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, recipeId);
    }
}
