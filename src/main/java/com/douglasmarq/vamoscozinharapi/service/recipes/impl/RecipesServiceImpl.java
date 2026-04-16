package com.douglasmarq.vamoscozinharapi.service.recipes.impl;

import static com.douglasmarq.vamoscozinharapi.utils.StringUtils.sanitizeString;
import static com.douglasmarq.vamoscozinharapi.utils.StringUtils.validateImageUrl;

import java.util.List;
import java.util.Objects;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.douglasmarq.vamoscozinharapi.repository.RecipeFavoriteRepository;
import com.douglasmarq.vamoscozinharapi.repository.RecipeLikeRepository;
import com.douglasmarq.vamoscozinharapi.repository.RecipesRepository;
import com.douglasmarq.vamoscozinharapi.repository.dto.IngredientsDTO;
import com.douglasmarq.vamoscozinharapi.repository.dto.PageResponse;
import com.douglasmarq.vamoscozinharapi.repository.dto.RecipeDTO;
import com.douglasmarq.vamoscozinharapi.repository.dto.RecipeSearchRequest;
import com.douglasmarq.vamoscozinharapi.repository.dto.recipes.RecipeDetailResponse;
import com.douglasmarq.vamoscozinharapi.repository.dto.recipes.RecipeSummary;
import com.douglasmarq.vamoscozinharapi.repository.entities.RecipesEntity;
import com.douglasmarq.vamoscozinharapi.service.comments.CommentsService;
import com.douglasmarq.vamoscozinharapi.service.recipes.HotRecipesService;
import com.douglasmarq.vamoscozinharapi.service.recipes.RecipesService;

@Service
public class RecipesServiceImpl implements RecipesService {

    private static final int DETAIL_COMMENTS_PAGE_SIZE = 10;

    private final RecipesRepository repository;
    private final RecipeLikeRepository likeRepository;
    private final RecipeFavoriteRepository favoriteRepository;
    private final CommentsService commentsService;
    private final HotRecipesService hotRecipesService;
    private final RecipesService self;

    public RecipesServiceImpl(
            RecipesRepository repository,
            RecipeLikeRepository likeRepository,
            RecipeFavoriteRepository favoriteRepository,
            CommentsService commentsService,
            HotRecipesService hotRecipesService,
            @Lazy RecipesService self) {
        this.repository = repository;
        this.likeRepository = likeRepository;
        this.favoriteRepository = favoriteRepository;
        this.commentsService = commentsService;
        this.hotRecipesService = hotRecipesService;
        this.self = self;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "recipes")
    public List<RecipeSummary> getAllRecipes() {
        return repository.getAllRecipes().stream().map(RecipeSummary::of).toList();
    }

    @Override
    @Transactional
    @Caching(
            evict = {
                @CacheEvict(value = "recipes", allEntries = true),
                @CacheEvict(value = "recipesSearch", allEntries = true)
            })
    public void createRecipe(RecipeDTO recipe) {
        RecipesEntity entity = new RecipesEntity();
        entity.setName(recipe.name());
        entity.setDescription(recipe.description());
        entity.setDifficulty(recipe.difficulty());
        entity.setImage(recipe.image());
        entity.setPrepare(recipe.prepare());

        List<IngredientsDTO> ingredients =
                recipe.ingredients().stream()
                        .filter(Objects::nonNull)
                        .map(RecipesServiceImpl::mapIngredient)
                        .toList();
        entity.setRecipeIngredients(ingredients);

        repository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "recipe", key = "#id", unless = "#result == null")
    public RecipesEntity getRecipeEntity(Long id) {
        return repository.getRecipeById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public RecipeDetailResponse getRecipeDetail(Long id, Long viewerUserId) {
        RecipesEntity entity = self.getRecipeEntity(id);
        if (entity == null) return null;
        hotRecipesService.incrementViewsAsync(id);
        Boolean likedByMe = viewerUserId == null ? null : likeRepository.exists(viewerUserId, id);
        Boolean favoritedByMe =
                viewerUserId == null ? null : favoriteRepository.exists(viewerUserId, id);
        Pageable commentsPageable =
                PageRequest.of(
                        0, DETAIL_COMMENTS_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createdAt"));
        return new RecipeDetailResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getDifficulty(),
                entity.getImage(),
                entity.getRecipeIngredients(),
                entity.getPrepare(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                nz(entity.getLikesCount()),
                nz(entity.getCommentsCount()),
                nz(entity.getFavoritesCount()),
                nz(entity.getViewsCount()),
                likedByMe,
                favoritedByMe,
                commentsService.listByRecipe(id, commentsPageable));
    }

    @Override
    @Transactional
    @Caching(
            evict = {
                @CacheEvict(value = "recipe", key = "#id"),
                @CacheEvict(value = "recipes", allEntries = true),
                @CacheEvict(value = "recipesSearch", allEntries = true),
                @CacheEvict(value = "hotRecipesByViews", allEntries = true),
                @CacheEvict(value = "hotRecipesByLikes", allEntries = true)
            })
    public boolean deleteRecipeById(Long id) {
        if (repository.getRecipeById(id) == null) return false;
        repository.deleteById(id);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(
            value = "recipesSearch",
            key =
                    "T(java.util.Objects).toString(#filters.id, '') + '|'"
                            + " + T(java.util.Objects).toString(#filters.q, '') + '|'"
                            + " + T(java.util.Objects).toString(#filters.difficulty, '') + '|'"
                            + " + #pageable.pageNumber + '|' + #pageable.pageSize + '|'"
                            + " + #pageable.sort.toString()")
    public PageResponse<RecipeSummary> searchRecipes(
            RecipeSearchRequest filters, Pageable pageable) {
        Page<RecipesEntity> page = repository.search(filters, pageable);
        Page<RecipeSummary> mapped =
                new PageImpl<>(
                        page.getContent().stream().map(RecipeSummary::of).toList(),
                        pageable,
                        page.getTotalElements());
        return PageResponse.of(mapped);
    }

    private static long nz(Long v) {
        return v == null ? 0L : v;
    }

    private static IngredientsDTO mapIngredient(IngredientsDTO ingredient) {
        return IngredientsDTO.of(
                sanitizeString(ingredient.ingredient()),
                sanitizeString(ingredient.description()),
                validateImageUrl(ingredient.image()));
    }
}
