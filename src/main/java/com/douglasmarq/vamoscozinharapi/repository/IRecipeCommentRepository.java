package com.douglasmarq.vamoscozinharapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.douglasmarq.vamoscozinharapi.repository.entities.RecipeCommentEntity;

public interface IRecipeCommentRepository extends JpaRepository<RecipeCommentEntity, Long> {

    @Query("SELECT c FROM RecipeCommentEntity c WHERE c.recipeId = :recipeId")
    Page<RecipeCommentEntity> findByRecipe(@Param("recipeId") Long recipeId, Pageable pageable);

    @Query("SELECT count(c) FROM RecipeCommentEntity c WHERE c.recipeId = :recipeId")
    long countByRecipe(@Param("recipeId") Long recipeId);
}
