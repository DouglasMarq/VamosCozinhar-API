package com.douglasmarq.vamoscozinharapi.repository.dto;

import java.io.Serializable;

public record RateRecipeDTO(Boolean liked, boolean viewed) implements Serializable {

    public static RateRecipeDTO of(Boolean liked, boolean viewed) {
        return new RateRecipeDTO(liked, viewed);
    }
}
