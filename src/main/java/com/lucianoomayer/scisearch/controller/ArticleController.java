package com.lucianoomayer.scisearch.controller;

import com.lucianoomayer.scisearch.model.Article;
import com.lucianoomayer.scisearch.service.ArticleService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;

@Validated
@RestController
@RequestMapping("api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public ResponseEntity<Flux<Article>> search(
            @RequestParam @NotBlank(message = "query must not be blank") String query,
            @RequestParam(required = false) Integer startYear,
            @RequestParam(required = false) Integer endYear) {

        if(startYear != null && endYear != null && startYear > endYear){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "startYear cannot be greater than endYear");
        }

        return ResponseEntity.ok(articleService.searchArticles(query, startYear, endYear));
    }
}
