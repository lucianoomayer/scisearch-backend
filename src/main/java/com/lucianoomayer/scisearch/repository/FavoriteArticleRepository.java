package com.lucianoomayer.scisearch.repository;

import com.lucianoomayer.scisearch.model.FavoriteArticle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteArticleRepository extends JpaRepository<FavoriteArticle, String> {
    List<FavoriteArticle> findByUserId(String userId);
    Optional<FavoriteArticle> findByUserIdAndArticleId(String userId, String articleId);
}
