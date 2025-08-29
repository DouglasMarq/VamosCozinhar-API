package com.douglasmarq.vamoscozinharapi.repository.dto;

import java.io.Serializable;

public record IngredientsDTO(String ingredient, String description, String image) implements Serializable {

    public static IngredientsDTO of(String ingredient, String description, String image) {
        return new IngredientsDTO(ingredient, description, image);
    }
}
