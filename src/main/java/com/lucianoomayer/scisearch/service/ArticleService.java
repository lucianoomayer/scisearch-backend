package com.lucianoomayer.scisearch.service;

import com.lucianoomayer.scisearch.integration.crossRef.service.CrossRefService;
import com.lucianoomayer.scisearch.integration.pubmed.service.PubMedService;
import com.lucianoomayer.scisearch.model.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final PubMedService pubMedService;
    private final CrossRefService crossRefService;

    public Flux<Article> searchArticles(String query, Integer minYear, Integer maxYear){
        return Flux.merge(
                pubMedService.fetchArticlesByQuery(query, minYear, maxYear),
                crossRefService.fetchArticlesByQuery(query, minYear, maxYear))
                .onErrorContinue((ex, obj) -> {
                    System.err.println("Erro ao buscar artigo: " + ex.getMessage() + " | API: " + obj);
                });
    }
}
