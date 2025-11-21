package com.lucianoomayer.scisearch.dto;

import java.sql.Timestamp;

public record ArticleResponseDTO(
        String articleId,
        String title,
        String url,
        String publicationDate,
        String source,
        Timestamp favoriteAt
) {}
