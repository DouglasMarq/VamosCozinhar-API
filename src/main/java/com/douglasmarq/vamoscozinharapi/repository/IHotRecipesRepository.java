package com.douglasmarq.vamoscozinharapi.repository;

import java.util.List;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.douglasmarq.vamoscozinharapi.repository.entities.HotRecipesEntity;

public interface IHotRecipesRepository extends JpaRepository<HotRecipesEntity, Long> {

    @Modifying
    @Query("UPDATE HotRecipesEntity h SET h.views = COALESCE(h.views, 0) + 1 WHERE h.id = :id")
    int incrementViews(@Param("id") Long id);

    List<HotRecipesEntity> findTopByOrderByViewsDesc(Limit limit);

    List<HotRecipesEntity> findTopByOrderByLikesDesc(Limit limit);
}
