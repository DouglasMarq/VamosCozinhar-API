package com.douglasmarq.vamoscozinharapi.service.recipes.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.douglasmarq.vamoscozinharapi.repository.HotRecipesRepository;
import com.douglasmarq.vamoscozinharapi.repository.dto.RateRecipeDTO;
import com.douglasmarq.vamoscozinharapi.repository.entities.HotRecipesEntity;
import com.douglasmarq.vamoscozinharapi.service.recipes.HotRecipesService;

@Service
public class HotRecipesServiceImpl implements HotRecipesService {
    private final HotRecipesRepository repository;

    public HotRecipesServiceImpl(HotRecipesRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<HotRecipesEntity> getHotRecipesByViews() {
        return repository.getHotRecipesByViews();
    }

    @Override
    public List<HotRecipesEntity> getHotRecipesByLikes() {
        return repository.getHotRecipesByLikes();
    }

    @Override
    public boolean rateHotRecipe(Long id, RateRecipeDTO payload) {
        return repository.rateHotRecipe(id, payload);
    }
}
