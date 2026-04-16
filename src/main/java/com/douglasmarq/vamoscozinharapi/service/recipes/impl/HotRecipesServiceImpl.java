package com.douglasmarq.vamoscozinharapi.service.recipes.impl;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.douglasmarq.vamoscozinharapi.repository.HotRecipesRepository;
import com.douglasmarq.vamoscozinharapi.repository.entities.HotRecipesEntity;
import com.douglasmarq.vamoscozinharapi.service.recipes.HotRecipesService;

@Service
public class HotRecipesServiceImpl implements HotRecipesService {
    private final HotRecipesRepository repository;

    public HotRecipesServiceImpl(HotRecipesRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "hotRecipesByViews", unless = "#result.isEmpty()")
    public List<HotRecipesEntity> getHotRecipesByViews() {
        return repository.getHotRecipesByViews();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "hotRecipesByLikes", unless = "#result.isEmpty()")
    public List<HotRecipesEntity> getHotRecipesByLikes() {
        return repository.getHotRecipesByLikes();
    }

    @Override
    @Async
    @Transactional
    @CacheEvict(value = "hotRecipesByViews", allEntries = true)
    public void incrementViewsAsync(Long recipeId) {
        repository.incrementViews(recipeId);
    }
}
