package com.douglasmarq.vamoscozinharapi.repository.dto;

public record IngredientsDTO(String ingredient, String description, String image) {

    public static IngredientsDTO of(String ingredient, String description, String image) {
        return new IngredientsDTO(ingredient, description, image);
    }
}
