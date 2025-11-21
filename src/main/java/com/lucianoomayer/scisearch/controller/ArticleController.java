package com.lucianoomayer.scisearch.controller;

import com.lucianoomayer.scisearch.dto.ArticleRequestDTO;
import com.lucianoomayer.scisearch.model.FavoriteArticle;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import com.lucianoomayer.scisearch.infra.security.TokenService;
import com.lucianoomayer.scisearch.model.Article;
import com.lucianoomayer.scisearch.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final TokenService tokenService;


    @GetMapping("/search")
    public Flux<Article> search(@RequestParam String query, @RequestParam(required = false) String startYear, @RequestParam(required = false) String endYear) {
        Integer minYear = Optional.ofNullable(startYear)
                .filter(s -> !s.isBlank())
                .map(Integer::parseInt)
                .orElse(null);

        Integer maxYear = Optional.ofNullable(endYear)
                .filter(s -> !s.isBlank())
                .map(Integer::parseInt)
                .orElse(null);

        if(minYear != null && maxYear != null && minYear > maxYear){
            return Flux.empty();
        }

        return articleService.searchArticles(query, minYear, maxYear);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveFavorite(@RequestBody ArticleRequestDTO request, @RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ", "");
        String userId = tokenService.validateToken(token);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }

        try{
            return articleService.saveFavorite(request, userId);
        }catch (DataIntegrityViolationException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Artigo já está nos favoritos.");
        }
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteArticle>> getFavorites(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userId = tokenService.validateToken(token);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<FavoriteArticle> favorites = articleService.getFavorite(userId);

        if(favorites.isEmpty()) return ResponseEntity.ok(Collections.emptyList());
        return ResponseEntity.ok(favorites);
    }

    @DeleteMapping("/favorites/delete")
    public ResponseEntity<?> deleteFavorite(@RequestParam String articleId, @RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ", "");
        String userId = tokenService.validateToken(token);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean deleted = articleService.deleteFavorite(userId, articleId);

        if (deleted) {
            return ResponseEntity.ok("Artigo removido com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Artigo não encontrado.");
        }
    }
}
