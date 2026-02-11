package com.lucianoomayer.scisearch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lucianoomayer.scisearch.model.Article;
import com.lucianoomayer.scisearch.model.ArticleFavorite;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

public record ArticleFavoriteResponseDTO(UUID id, @JsonProperty("article") ArticleSummaryDTO article, LocalDate favoriteAt){
    public static ArticleFavoriteResponseDTO from(ArticleFavorite fav){
        return new ArticleFavoriteResponseDTO(
                fav.getId(),
                ArticleSummaryDTO.from(fav.getArticle()),
                fav.getFavoriteAt()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()

        );
    }
}

record ArticleSummaryDTO(
        String externalId,
        String title,
        String url,
        Integer publicationYear
){
    public static ArticleSummaryDTO from(Article article){
        return new ArticleSummaryDTO(
                article.getExternalId(),
                article.getTitle(),
                article.getUrl(),
                article.getPublicationYear()
        );
    }
}

