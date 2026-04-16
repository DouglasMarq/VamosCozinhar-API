package com.douglasmarq.vamoscozinharapi.repository.dto.recipes;

import java.io.Serializable;

public record LikeStatusResponse(Boolean liked, long likesCount) implements Serializable {}
