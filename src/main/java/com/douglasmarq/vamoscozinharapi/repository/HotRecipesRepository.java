package com.douglasmarq.vamoscozinharapi.repository;

import java.util.List;

import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Repository;

import com.douglasmarq.vamoscozinharapi.repository.entities.HotRecipesEntity;

@Repository
public class HotRecipesRepository {

    private final IHotRecipesRepository repository;

    public HotRecipesRepository(IHotRecipesRepository repository) {
        this.repository = repository;
    }

    public List<HotRecipesEntity> getHotRecipesByViews() {
        return repository.findTopByOrderByViewsDesc(Limit.of(5));
    }

    public List<HotRecipesEntity> getHotRecipesByLikes() {
        return repository.findTopByOrderByLikesDesc(Limit.of(5));
    }

    public int incrementViews(Long id) {
        return repository.incrementViews(id);
    }
}
