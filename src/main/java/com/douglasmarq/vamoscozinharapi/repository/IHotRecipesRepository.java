package com.douglasmarq.vamoscozinharapi.repository;

import java.util.List;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.douglasmarq.vamoscozinharapi.repository.entities.HotRecipesEntity;

public interface IHotRecipesRepository extends JpaRepository<HotRecipesEntity, Long> {

    @Modifying
    @Transactional
    @Query(
            """
                UPDATE HotRecipesEntity h
                SET h.likes = h.likes + CASE
                WHEN :liked IS NOT NULL AND :liked = true THEN 1
                WHEN :liked IS NOT NULL AND :liked = false THEN -1
                ELSE 0 END,
                h.views = h.views + 1
                WHERE h.id = :id
            """)
    int rateHotRecipe(@Param("id") Long id, @Param("liked") Boolean liked);

    List<HotRecipesEntity> findTopByOrderByViewsDesc(Limit limit);

    List<HotRecipesEntity> findTopByOrderByLikesDesc(Limit limit);
}
