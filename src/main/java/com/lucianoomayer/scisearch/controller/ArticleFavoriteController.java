package com.lucianoomayer.scisearch.controller;

import com.lucianoomayer.scisearch.dto.ArticleFavoriteResponseDTO;
import com.lucianoomayer.scisearch.dto.ArticleRequestDTO;
import com.lucianoomayer.scisearch.model.User;
import com.lucianoomayer.scisearch.service.ArticleFavoriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/favorites")
@RequiredArgsConstructor
public class ArticleFavoriteController {

    private final ArticleFavoriteService articleFavoriteService;

    @PostMapping
    public ResponseEntity<ArticleFavoriteResponseDTO> saveFavorite(@RequestBody @Valid ArticleRequestDTO request, @AuthenticationPrincipal User user){
        return ResponseEntity.status(HttpStatus.CREATED).body(articleFavoriteService.saveArticle(request, user));
    }

    @GetMapping
    public ResponseEntity<List<ArticleFavoriteResponseDTO>> getFavorites(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(articleFavoriteService.getAllFavorites(user));
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable UUID favoriteId, @AuthenticationPrincipal User user) throws Exception{
        articleFavoriteService.deleteFavorite(favoriteId, user);
        return ResponseEntity.noContent().build();
    }
}
