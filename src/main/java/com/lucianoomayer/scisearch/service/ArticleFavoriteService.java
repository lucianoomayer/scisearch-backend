package com.lucianoomayer.scisearch.service;

import com.lucianoomayer.scisearch.dto.ArticleFavoriteResponseDTO;
import com.lucianoomayer.scisearch.dto.ArticleRequestDTO;
import com.lucianoomayer.scisearch.model.Article;
import com.lucianoomayer.scisearch.model.ArticleFavorite;
import com.lucianoomayer.scisearch.model.User;
import com.lucianoomayer.scisearch.repository.ArticleFavoriteRepository;
import com.lucianoomayer.scisearch.repository.ArticleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArticleFavoriteService {
    private final ArticleFavoriteRepository articleFavoriteRepository;
    private final ArticleRepository articleRepository;

    public ArticleFavoriteResponseDTO saveArticle(ArticleRequestDTO request, User user){
        Article article = articleRepository.findByExternalId(request.externalId())
                .orElseGet(() -> {
                    Article newArticle = Article.builder()
                            .externalId(request.externalId())
                            .title(request.title())
                            .url(request.url())
                            .publicationYear(request.publicationYear())
                            .source(request.source())
                            .build();
                    return articleRepository.save(newArticle);
                });

        if(articleFavoriteRepository.existsByUserAndArticle(user, article)){
            throw new RuntimeException();
        }
        ArticleFavorite favorite = new ArticleFavorite(user, article);
        return ArticleFavoriteResponseDTO.from(articleFavoriteRepository.save(favorite));
    }

    public List<ArticleFavoriteResponseDTO> getAllFavorites(User user){
        return articleFavoriteRepository.findByUser(user)
                .stream()
                .map(ArticleFavoriteResponseDTO::from)
                .toList();
    }

    public void deleteFavorite(UUID favoriteId, User user) throws AccessDeniedException {
        ArticleFavorite favorite = articleFavoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new EntityNotFoundException("Favorite not found"));

        if(!favorite.getUser().getId().equals(user.getId())){
            throw new AccessDeniedException("You cannot delete this favorite");
        }
        articleFavoriteRepository.delete(favorite);
    }
}
