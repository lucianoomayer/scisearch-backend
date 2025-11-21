package com.lucianoomayer.scisearch.integration.pubmed.service;

import com.lucianoomayer.scisearch.integration.pubmed.dto.PubMedArticleDTO;
import com.lucianoomayer.scisearch.integration.pubmed.dto.PmidSearchResultDTO;
import com.lucianoomayer.scisearch.integration.pubmed.dto.MetaDataResultDTO;
import com.lucianoomayer.scisearch.model.Article;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

@Service
public class PubMedService {

    private final WebClient webClient;

    @Value("${pubmed.api.key}")
    private String apiKey;

    public PubMedService(@Qualifier("pubMedWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<Article> fetchArticlesByQuery(String term, Integer minYear, Integer maxYear) {
        return searchPmids(term, minYear, maxYear)
                .buffer(200)
                .delayElements(Duration.ofMillis(200))
                .flatMap(this::fetchMetaData, 2)
                .map(this::mapToArticle)
                .filter(article -> {
                    if(article.getPublicationDate().isEmpty()) return true;
                    int year = Integer.parseInt(article.getPublicationDate());
                    return (minYear == null || year >= minYear) &&
                            (maxYear == null || year <= maxYear);
                });
    }

    private Flux<String> searchPmids(String term, Integer minYear, Integer maxYear) {
        String encodedTerm = URLEncoder.encode(term, StandardCharsets.UTF_8);

        String searchUri = String.format(
                "esearch.fcgi?db=pubmed&retmode=json&retmax=100&term=%s&datetype=pdat&api_key=%s",
                encodedTerm,
                apiKey
        );

        if(minYear != null) searchUri += "&mindate=" + minYear;
        if(maxYear != null) searchUri += "&maxdate=" + maxYear;

        Mono<PmidSearchResultDTO> firstCall = webClient.get()
                .uri(searchUri + "&retstart=0")
                .retrieve()
                .bodyToMono(PmidSearchResultDTO.class);

        String finalSearchUri = searchUri;
        return firstCall.flatMapMany(result -> {
            int total = result.searchResult().count();
            int pages = (int) Math.ceil(total / 100.0);
            int maxPages = Math.min(pages, 5);

            return Flux.range(0, maxPages)
                    .flatMap(page -> webClient.get()
                            .uri(finalSearchUri + "&retstart=" + (page * 100))
                            .retrieve()
                            .bodyToMono(PmidSearchResultDTO.class)
                            .map(r -> r.searchResult().pmid())
                    )
                    .flatMapIterable(list -> list);
        });
    }

    private Flux<PubMedArticleDTO> fetchMetaData(List<String> pmid){
        if (pmid.isEmpty()) return Flux.empty();

        String pmids = String.join(",", pmid);
        String fetchUrl = String.format(
                "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?db=pubmed&id=%s&retmode=json",
                pmids
        );

        return webClient.get()
                .uri(fetchUrl)
                .retrieve()
                .bodyToMono(MetaDataResultDTO.class)
                .flatMapMany(metaData -> Flux.fromIterable(metaData.getResult().getArticlesList()));
    }

    private Article mapToArticle(PubMedArticleDTO dto){
        Article article = new Article();

        article.setTitle(dto.title() != null && !dto.title().isEmpty()
                ? dto.title()
                : null);

        article.setArticleId(dto.pmid());

        article.setArticleUrl(dto.getPubMedUrl());

        article.setAuthors(
                dto.authors() != null
                        ? dto.authors().stream().map(PubMedArticleDTO.AuthorDTO::name).toList()
                        : List.of()
        );

        article.setAbstractText("");

        String year = "";
        if(!dto.pubDate().isEmpty()){
            year = dto.pubDate().substring(0, 4);
        }else if(!dto.epubDate().isEmpty()){
            year = dto.epubDate().substring(0, 4);
        }

        article.setPublicationDate(year);

        article.setSource("PubMed");

        return article;
    }
}
