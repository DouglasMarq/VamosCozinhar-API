package com.douglasmarq.vamoscozinharapi.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

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

    public boolean deleteById(Long id) {
        repository.deleteById(id);

        return true;
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
