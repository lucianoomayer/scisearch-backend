package com.lucianoomayer.scisearch.repository;

import com.lucianoomayer.scisearch.model.Article;
import com.lucianoomayer.scisearch.model.ArticleFavorite;
import com.lucianoomayer.scisearch.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ArticleFavoriteRepository extends JpaRepository<ArticleFavorite, UUID> {
    boolean existsByUserAndArticle(User user, Article article);

    List<ArticleFavorite> findByUser(User user);
}
