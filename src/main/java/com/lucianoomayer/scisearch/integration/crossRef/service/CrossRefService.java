package com.lucianoomayer.scisearch.integration.crossRef.service;

import com.lucianoomayer.scisearch.integration.crossRef.dto.CrossRefResponseDTO;
import com.lucianoomayer.scisearch.integration.crossRef.dto.CrossRefArticleDTO;
import com.lucianoomayer.scisearch.model.Article;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import java.util.List;

@Slf4j
@Service
public class CrossRefService {

    private final WebClient webClient;

    public CrossRefService(@Qualifier("crossRefWebClient") WebClient webClient){
        this.webClient = webClient;
    }

    public Flux<Article> fetchArticlesByQuery(String term, Integer minYear, Integer maxYear){

        String searchUri = String.format("?query=%s&rows=100&filter=type:journal-article",
                term.replace(" ", "+")
        );

        if (minYear != null) searchUri += ",from-pub-date:" + minYear;
        if (maxYear != null) searchUri += ",until-pub-date:" + maxYear;

        return webClient.get()
                .uri(searchUri)
                .retrieve()
                .bodyToMono(CrossRefResponseDTO.class)
                .flatMapMany(dto -> Flux.fromIterable(dto.message().items()))
                .filter(dto -> dto.getTitle() != null && !dto.getTitle().isEmpty())
                .map(this::mapToArticle)
                .filter(article -> {
                    if(article.getPublicationDate().isEmpty()) return true;
                    int year = Integer.parseInt(article.getPublicationDate());
                    return (minYear == null || year >= minYear) &&
                            (maxYear == null || year <= maxYear);
                });
    }

    private Article mapToArticle(CrossRefArticleDTO dto){
        Article article = new Article();

        article.setTitle(dto.getTitle() != null && !dto.getTitle().isEmpty()
                ? dto.getTitle().get(0)
                : null);

        article.setArticleId(dto.getArticleId());

        article.setArticleUrl(dto.getUrl());

        article.setAuthors(
                dto.getAuthor() != null
                        ? dto.getAuthor().stream().map(CrossRefArticleDTO.Author::getFullName).toList()
                        : List.of()
        );

        article.setAbstractText("");

        String publicationDate = "";
        if (dto.getPublishedOnline() != null) {
            publicationDate = dto.getPublishedOnline().extractYear();
        } else if (dto.getPublishedPrint() != null) {
            publicationDate = dto.getPublishedPrint().extractYear();
        }
        article.setPublicationDate(publicationDate);

        article.setSource("CrossRef");

        return article;
    }

}
