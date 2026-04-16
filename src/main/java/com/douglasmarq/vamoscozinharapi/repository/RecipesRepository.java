package com.douglasmarq.vamoscozinharapi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.douglasmarq.vamoscozinharapi.repository.dto.RecipeSearchRequest;
import com.douglasmarq.vamoscozinharapi.repository.entities.RecipesEntity;

@Repository
public class RecipesRepository {

    private final IRecipesRepository repository;

    public RecipesRepository(IRecipesRepository repository) {
        this.repository = repository;
    }

    public List<RecipesEntity> getAllRecipes() {
        return repository.findAll();
    }

    public RecipesEntity getRecipeById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public RecipesEntity save(RecipesEntity entity) {
        return repository.save(entity);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public Page<RecipesEntity> search(RecipeSearchRequest filters, Pageable pageable) {
        return repository.search(filters.id(), filters.q(), filters.difficulty(), pageable);
    }
}
