package com.douglasmarq.vamoscozinharapi.repository.entities;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.douglasmarq.vamoscozinharapi.repository.dto.IngredientsDTO;

@Entity
@Table(name = "recipes")
public class RecipesEntity implements Serializable {
    @Serial private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recipes_id_seq")
    @SequenceGenerator(name = "recipes_id_seq", sequenceName = "recipes_id_seq", allocationSize = 1)
    private Long id;

    private String name;

    private String description;

    private Integer difficulty;

    private String image;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "recipe_ingredients", columnDefinition = "jsonb")
    private List<IngredientsDTO> recipeIngredients = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "prepare", columnDefinition = "jsonb")
    private List<String> prepare = new ArrayList<>();

    @Formula("(SELECT COALESCE(hr.likes, 0) FROM hot_recipes hr WHERE hr.recipe_id = id)")
    private Long likesCount;

    @Formula("(SELECT COALESCE(hr.views, 0) FROM hot_recipes hr WHERE hr.recipe_id = id)")
    private Long viewsCount;

    @Formula("(SELECT count(*) FROM recipe_comments rc WHERE rc.recipe_id = id)")
    private Long commentsCount;

    @Formula("(SELECT count(*) FROM recipe_favorites rf WHERE rf.recipe_id = id)")
    private Long favoritesCount;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipesEntity that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<IngredientsDTO> getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(List<IngredientsDTO> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public List<String> getPrepare() {
        return prepare;
    }

    public void setPrepare(List<String> prepare) {
        this.prepare = prepare;
    }

    public Long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Long likesCount) {
        this.likesCount = likesCount;
    }

    public Long getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(Long viewsCount) {
        this.viewsCount = viewsCount;
    }

    public Long getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Long commentsCount) {
        this.commentsCount = commentsCount;
    }

    public Long getFavoritesCount() {
        return favoritesCount;
    }

    public void setFavoritesCount(Long favoritesCount) {
        this.favoritesCount = favoritesCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
