package com.douglasmarq.vamoscozinharapi.repository.dto;

import java.util.List;

public record RecipeDTO(
        String name,
        String description,
        Integer difficulty,
        String image,
        List<IngredientsDTO> ingredients,
        List<String> prepare) {

    public static RecipeDTO of(
            String name,
            String description,
            Integer difficulty,
            String image,
            List<IngredientsDTO> ingredients,
            List<String> prepare) {
        return new RecipeDTO(name, description, difficulty, image, ingredients, prepare);
    }
}
