package com.douglasmarq.vamoscozinharapi.repository.dto;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RecipeDTO(
        @NotBlank(message = "Recipe name cannot be blank") String name,
        @NotBlank(message = "Recipe description cannot be blank") String description,
        @NotNull(message = "Difficulty level is required") Integer difficulty,
        String image,
        @Valid List<IngredientsDTO> ingredients,
        List<String> prepare) implements Serializable {

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
