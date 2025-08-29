package com.douglasmarq.vamoscozinharapi.service.recipes;

import java.util.List;

import com.douglasmarq.vamoscozinharapi.repository.dto.RateRecipeDTO;
import com.douglasmarq.vamoscozinharapi.repository.entities.HotRecipesEntity;

public interface HotRecipesService {
    List<HotRecipesEntity> getHotRecipesByViews();

    List<HotRecipesEntity> getHotRecipesByLikes();

    boolean rateHotRecipe(Long id, RateRecipeDTO payload);
}
