package com.douglasmarq.vamoscozinharapi.controller.v1.recipes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.douglasmarq.vamoscozinharapi.repository.RecipesRepository;
import com.douglasmarq.vamoscozinharapi.repository.dto.IngredientsDTO;
import com.douglasmarq.vamoscozinharapi.repository.dto.RecipeDTO;
import com.douglasmarq.vamoscozinharapi.repository.entities.RecipesEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Transactional
public class RecipesControllerTestIT {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17.6-alpine3.22")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @Container
    static GenericContainer<?> valkey =
            new GenericContainer<>("valkey/valkey:8.1.3-alpine3.22").withExposedPorts(6379);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");

        registry.add("spring.flyway.url", postgres::getJdbcUrl);
        registry.add("spring.flyway.user", postgres::getUsername);
        registry.add("spring.flyway.password", postgres::getPassword);

        registry.add("spring.data.redis.host", valkey::getHost);
        registry.add("spring.data.redis.port", valkey::getFirstMappedPort);
    }

    @Autowired private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired private RecipesRepository recipesRepository;

    @Autowired private ObjectMapper objectMapper;

    private RecipeDTO sampleRecipeDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        recipesRepository.deleteAll();

        List<IngredientsDTO> ingredients =
                List.of(
                        IngredientsDTO.of("Flour", "2 cups of all-purpose flour", "flour.jpg"),
                        IngredientsDTO.of("Sugar", "1 cup of white sugar", "sugar.jpg"),
                        IngredientsDTO.of("Eggs", "3 large eggs", "eggs.jpg"));

        List<String> prepare =
                List.of(
                        "Preheat oven to 350°F",
                        "Mix dry ingredients in a bowl",
                        "Add wet ingredients and mix well",
                        "Bake for 25-30 minutes");

        sampleRecipeDTO =
                RecipeDTO.of(
                        "Chocolate Cake",
                        "A delicious chocolate cake recipe",
                        3,
                        "chocolate-cake.jpg",
                        ingredients,
                        prepare);
    }

    @Test
    void shouldGetAllRecipes() throws Exception {
        RecipesEntity recipe1 = createRecipeEntity("Recipe 1", "Description 1", 2);
        RecipesEntity recipe2 = createRecipeEntity("Recipe 2", "Description 2", 4);
        recipesRepository.save(recipe1);
        recipesRepository.save(recipe2);

        mockMvc.perform(get("/v1/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Recipe 1"))
                .andExpect(jsonPath("$[1].name").value("Recipe 2"));
    }

    @Test
    void shouldGetRecipeById() throws Exception {
        RecipesEntity savedRecipe =
                recipesRepository.save(createRecipeEntity("Test Recipe", "Test Description", 3));

        mockMvc.perform(get("/v1/recipes/{id}", savedRecipe.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedRecipe.getId()))
                .andExpect(jsonPath("$.name").value("Test Recipe"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.difficulty").value(3));
    }

    @Test
    void shouldReturnNotFoundWhenRecipeDoesNotExist() throws Exception {
        // When & Then
        mockMvc.perform(get("/v1/recipes/{id}", 999L)).andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateRecipe() throws Exception {
        String recipeJson = objectMapper.writeValueAsString(sampleRecipeDTO);

        mockMvc.perform(
                        post("/v1/recipes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(recipeJson))
                .andExpect(status().isCreated())
                .andExpect(content().string("Recipe created successfully"));

        List<RecipesEntity> recipes = recipesRepository.getAllRecipes();
        assertThat(recipes).hasSize(1);
        RecipesEntity savedRecipe = recipes.get(0);
        assertThat(savedRecipe.getName()).isEqualTo("Chocolate Cake");
        assertThat(savedRecipe.getDescription()).isEqualTo("A delicious chocolate cake recipe");
        assertThat(savedRecipe.getDifficulty()).isEqualTo(3);
        assertThat(savedRecipe.getRecipeIngredients()).hasSize(3);
        assertThat(savedRecipe.getPrepare()).hasSize(4);
    }

    @Test
    void shouldReturnBadRequestWhenCreateRecipeWithInvalidData() throws Exception {
        RecipeDTO invalidRecipe =
                RecipeDTO.of(null, "Description", 3, "http://image.jpg", List.of(), List.of());
        String recipeJson = objectMapper.writeValueAsString(invalidRecipe);

        mockMvc.perform(
                        post("/v1/recipes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(recipeJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteRecipeById() throws Exception {
        RecipesEntity savedRecipe =
                recipesRepository.save(createRecipeEntity("Recipe to Delete", "Description", 2));

        mockMvc.perform(delete("/v1/recipes/{id}", savedRecipe.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Recipe deleted successfully"));

        assertThat(recipesRepository.getRecipeById(savedRecipe.getId())).isNull();
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentRecipe() throws Exception {
        mockMvc.perform(delete("/v1/recipes/{id}", 999L)).andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateRecipeWithComplexIngredients() throws Exception {
        List<IngredientsDTO> complexIngredients =
                List.of(
                        IngredientsDTO.of(
                                "Chocolate",
                                "200g dark chocolate (70% cocoa)",
                                "dark-chocolate.jpg"),
                        IngredientsDTO.of("Butter", "100g unsalted butter", "butter.jpg"),
                        IngredientsDTO.of(
                                "Vanilla Extract", "1 tsp pure vanilla extract", "vanilla.jpg"));

        RecipeDTO complexRecipe =
                RecipeDTO.of(
                        "Advanced Chocolate Dessert",
                        "A sophisticated chocolate dessert with multiple layers",
                        5,
                        "advanced-dessert.jpg",
                        complexIngredients,
                        List.of(
                                "Melt chocolate and butter in double boiler",
                                "Add vanilla extract and mix thoroughly",
                                "Cool mixture for 2 hours",
                                "Shape into desired form",
                                "Chill overnight before serving"));

        String recipeJson = objectMapper.writeValueAsString(complexRecipe);

        mockMvc.perform(
                        post("/v1/recipes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(recipeJson))
                .andExpect(status().isCreated());

        List<RecipesEntity> recipes = recipesRepository.getAllRecipes();
        assertThat(recipes).hasSize(1);
        RecipesEntity savedRecipe = recipes.get(0);
        assertThat(savedRecipe.getRecipeIngredients()).hasSize(3);
        assertThat(savedRecipe.getRecipeIngredients().get(0).ingredient()).isEqualTo("Chocolate");
        assertThat(savedRecipe.getRecipeIngredients().get(0).description())
                .isEqualTo("200g dark chocolate (70% cocoa)");
        assertThat(savedRecipe.getPrepare()).hasSize(5);
        assertThat(savedRecipe.getPrepare().get(0))
                .isEqualTo("Melt chocolate and butter in double boiler");
    }

    private RecipesEntity createRecipeEntity(String name, String description, Integer difficulty) {
        RecipesEntity recipe = new RecipesEntity();
        recipe.setName(name);
        recipe.setDescription(description);
        recipe.setDifficulty(difficulty);
        recipe.setImage("test-image.jpg");
        recipe.setRecipeIngredients(
                List.of(IngredientsDTO.of("Test Ingredient", "Test Description", "test.jpg")));
        recipe.setPrepare(List.of("Test preparation step"));
        return recipe;
    }
}
