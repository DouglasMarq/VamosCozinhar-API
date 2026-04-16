package com.douglasmarq.vamoscozinharapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.douglasmarq.vamoscozinharapi.repository.entities.RecipeLikeEntity;
import com.douglasmarq.vamoscozinharapi.repository.entities.RecipeLikeId;

public interface IRecipeLikeRepository extends JpaRepository<RecipeLikeEntity, RecipeLikeId> {

    @Query(
            "SELECT count(l) > 0 FROM RecipeLikeEntity l"
                    + " WHERE l.userId = :userId AND l.recipeId = :recipeId")
    boolean existsByUserAndRecipe(@Param("userId") Long userId, @Param("recipeId") Long recipeId);

    @Query("SELECT count(l) FROM RecipeLikeEntity l WHERE l.recipeId = :recipeId")
    long countByRecipe(@Param("recipeId") Long recipeId);

    @Modifying
    @Query(
            "DELETE FROM RecipeLikeEntity l"
                    + " WHERE l.userId = :userId AND l.recipeId = :recipeId")
    int deleteByUserAndRecipe(@Param("userId") Long userId, @Param("recipeId") Long recipeId);
}
