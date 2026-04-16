package com.douglasmarq.vamoscozinharapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.douglasmarq.vamoscozinharapi.repository.entities.RecipesEntity;

public interface IRecipesRepository extends JpaRepository<RecipesEntity, Long> {

    @Query(
            """
            SELECT r FROM RecipesEntity r
            WHERE (:id IS NULL OR r.id = :id)
              AND (:difficulty IS NULL OR r.difficulty = :difficulty)
              AND (:q IS NULL
                   OR lower(function('immutable_unaccent', r.name))
                      LIKE lower(function('immutable_unaccent', concat('%', :q, '%')))
                   OR lower(function('immutable_unaccent', r.description))
                      LIKE lower(function('immutable_unaccent', concat('%', :q, '%'))))
            """)
    Page<RecipesEntity> search(
            @Param("id") Long id,
            @Param("q") String q,
            @Param("difficulty") Integer difficulty,
            Pageable pageable);
}
