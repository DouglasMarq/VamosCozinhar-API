package com.douglasmarq.vamoscozinharapi.repository.dto;

public record RateRecipeDTO(Boolean liked, boolean viewed) {

    public static RateRecipeDTO of(Boolean liked, boolean viewed) {
        return new RateRecipeDTO(liked, viewed);
    }
}
