package com.douglasmarq.vamoscozinharapi.repository.dto;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RecipeDTO(
        @NotBlank(message = "{validation.recipe.name.required}") String name,
        @NotBlank(message = "{validation.recipe.description.required}") String description,
        @NotNull(message = "{validation.recipe.difficulty.required}") Integer difficulty,
        String image,
        @Valid List<IngredientsDTO> ingredients,
        List<String> prepare)
        implements Serializable {

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
