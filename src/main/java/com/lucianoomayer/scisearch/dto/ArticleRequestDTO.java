package com.lucianoomayer.scisearch.dto;

public record ArticleRequestDTO(
        String articleId,
        String title,
        String url,
        String source,
        String publicationDate) {
}
