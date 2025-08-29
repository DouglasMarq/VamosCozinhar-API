package com.douglasmarq.vamoscozinharapi.repository.entities;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
    private List<IngredientsDTO> RecipeIngredients = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "prepare", columnDefinition = "jsonb")
    private List<String> prepare = new ArrayList<>();

    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        Date now = Date.from(Instant.now());
        this.createdAt = now;
        this.updatedAt = now;
    }

    private Date updatedAt;

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Date.from(Instant.now());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RecipesEntity that = (RecipesEntity) o;
        return Objects.equals(getId(), that.getId())
                && Objects.equals(getName(), that.getName())
                && Objects.equals(getDescription(), that.getDescription())
                && Objects.equals(getDifficulty(), that.getDifficulty())
                && Objects.equals(getImage(), that.getImage())
                && Objects.equals(getRecipeIngredients(), that.getRecipeIngredients())
                && Objects.equals(getPrepare(), that.getPrepare())
                && Objects.equals(getCreatedAt(), that.getCreatedAt())
                && Objects.equals(getUpdatedAt(), that.getUpdatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getId(),
                getName(),
                getDescription(),
                getDifficulty(),
                getImage(),
                getRecipeIngredients(),
                getPrepare(),
                getCreatedAt(),
                getUpdatedAt());
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<String> getPrepare() {
        return prepare;
    }

    public void setPrepare(List<String> prepare) {
        this.prepare = prepare;
    }

    public List<IngredientsDTO> getRecipeIngredients() {
        return RecipeIngredients;
    }

    public void setRecipeIngredients(List<IngredientsDTO> recipeIngredients) {
        RecipeIngredients = recipeIngredients;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
