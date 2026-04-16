package com.douglasmarq.vamoscozinharapi.repository.dto.recipes;

import java.io.Serializable;

public record FavoriteStatusResponse(Boolean favorited, long favoritesCount)
        implements Serializable {}
