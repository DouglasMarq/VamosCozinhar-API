package com.douglasmarq.vamoscozinharapi.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.douglasmarq.vamoscozinharapi.repository.entities.RecipeCommentEntity;

@Repository
public class RecipeCommentRepository {

    private final IRecipeCommentRepository repository;

    public RecipeCommentRepository(IRecipeCommentRepository repository) {
        this.repository = repository;
    }

    public Page<RecipeCommentEntity> findByRecipe(Long recipeId, Pageable pageable) {
        return repository.findByRecipe(recipeId, pageable);
    }

    public long countByRecipe(Long recipeId) {
        return repository.countByRecipe(recipeId);
    }

    public Optional<RecipeCommentEntity> findById(Long id) {
        return repository.findById(id);
    }

    public RecipeCommentEntity save(RecipeCommentEntity entity) {
        return repository.save(entity);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
