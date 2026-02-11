package com.lucianoomayer.scisearch.repository;

import com.lucianoomayer.scisearch.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ArticleRepository extends JpaRepository<Article, UUID> {
    Optional<Article> findByExternalId(String externalId);
}
