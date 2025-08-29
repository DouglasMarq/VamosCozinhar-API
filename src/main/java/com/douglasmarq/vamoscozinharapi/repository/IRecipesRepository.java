package com.douglasmarq.vamoscozinharapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.douglasmarq.vamoscozinharapi.repository.entities.RecipesEntity;

public interface IRecipesRepository extends JpaRepository<RecipesEntity, Long> {}
