package com.douglasmarq.vamoscozinharapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.douglasmarq.vamoscozinharapi.repository.entities.RecipeFavoriteEntity;
import com.douglasmarq.vamoscozinharapi.repository.entities.RecipeFavoriteId;
import com.douglasmarq.vamoscozinharapi.repository.entities.RecipesEntity;

public interface IRecipeFavoriteRepository
        extends JpaRepository<RecipeFavoriteEntity, RecipeFavoriteId> {

    @Query(
            "SELECT count(f) > 0 FROM RecipeFavoriteEntity f"
                    + " WHERE f.userId = :userId AND f.recipeId = :recipeId")
    boolean existsByUserAndRecipe(@Param("userId") Long userId, @Param("recipeId") Long recipeId);

    @Query("SELECT count(f) FROM RecipeFavoriteEntity f WHERE f.recipeId = :recipeId")
    long countByRecipe(@Param("recipeId") Long recipeId);

    @Modifying
    @Query(
            "DELETE FROM RecipeFavoriteEntity f"
                    + " WHERE f.userId = :userId AND f.recipeId = :recipeId")
    int deleteByUserAndRecipe(@Param("userId") Long userId, @Param("recipeId") Long recipeId);

    @Query(
            "SELECT r FROM RecipesEntity r"
                    + " WHERE r.id IN ("
                    + "  SELECT f.recipeId FROM RecipeFavoriteEntity f WHERE f.userId = :userId"
                    + ")")
    Page<RecipesEntity> findFavoriteRecipes(@Param("userId") Long userId, Pageable pageable);
}
