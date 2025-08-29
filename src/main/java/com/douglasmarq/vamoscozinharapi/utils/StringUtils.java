package com.douglasmarq.vamoscozinharapi.utils;

public abstract class StringUtils {
    public static String sanitizeString(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }
        return input.trim();
    }

    public static String validateImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            return null;
        }

        String trimmed = imageUrl.trim();
        if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
            return trimmed;
        }
        return null;
    }
}
