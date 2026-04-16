package com.douglasmarq.vamoscozinharapi.repository.dto;

import java.io.Serializable;

public record RecipeSearchRequest(Long id, String q, Integer difficulty) implements Serializable {

    public static RecipeSearchRequest of(Long id, String q, Integer difficulty) {
        String normalized = (q == null || q.isBlank()) ? null : q.trim();
        return new RecipeSearchRequest(id, normalized, difficulty);
    }
}
