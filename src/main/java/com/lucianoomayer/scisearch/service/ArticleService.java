package com.lucianoomayer.scisearch.service;

import com.lucianoomayer.scisearch.dto.ArticleRequestDTO;
import com.lucianoomayer.scisearch.dto.ArticleResponseDTO;
import com.lucianoomayer.scisearch.integration.crossRef.service.CrossRefService;
import com.lucianoomayer.scisearch.integration.pubmed.service.PubMedService;
import com.lucianoomayer.scisearch.model.Article;
import com.lucianoomayer.scisearch.model.FavoriteArticle;
import com.lucianoomayer.scisearch.repository.FavoriteArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final PubMedService pubMedService;
    private final CrossRefService crossRefService;
    private final FavoriteArticleRepository favoriteArticleRepository;

    public Flux<Article> searchArticles(String query, Integer minYear, Integer maxYear){
        return Flux.merge(
                pubMedService.fetchArticlesByQuery(query, minYear, maxYear),
                crossRefService.fetchArticlesByQuery(query, minYear, maxYear))
                .onErrorContinue((ex, obj) -> {
                    System.err.println("Erro ao buscar artigo: " + ex.getMessage() + " | API: " + obj);
                });
    }

    public ResponseEntity<?> saveFavorite(ArticleRequestDTO request, String userId){
        FavoriteArticle favoriteArticle = new FavoriteArticle();

        favoriteArticle.setUserId(userId);
        favoriteArticle.setArticleId(request.articleId());
        favoriteArticle.setTitle(request.title());
        favoriteArticle.setUrl(request.url());
        favoriteArticle.setSource(request.source());
        favoriteArticle.setPublicationDate(request.publicationDate());
        favoriteArticle.setFavoriteAt(new Timestamp(System.currentTimeMillis()));

        try {
            FavoriteArticle favorite = favoriteArticleRepository.save(favoriteArticle);

            ArticleResponseDTO response = new ArticleResponseDTO(
                    favorite.getArticleId(),
                    favorite.getTitle(),
                    favorite.getUrl(),
                    favorite.getPublicationDate(),
                    favorite.getSource(),
                    favorite.getFavoriteAt()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Este artigo já foi favoritado.");
        }
    }

    public List<FavoriteArticle> getFavorite(String userId){
        return favoriteArticleRepository.findByUserId(userId);
    }

    public boolean deleteFavorite(String userId, String articleId){
        Optional<FavoriteArticle> article = favoriteArticleRepository.findByUserIdAndArticleId(userId, articleId);

        if (article.isPresent()) {
            favoriteArticleRepository.delete(article.get());
            return true;
        }
        return false;
    }
}
