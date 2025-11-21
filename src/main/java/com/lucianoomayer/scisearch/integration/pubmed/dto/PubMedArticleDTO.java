package com.lucianoomayer.scisearch.integration.pubmed.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PubMedArticleDTO(
        @JsonProperty("uid") String pmid,
        String title,
        String articleUrl,
        List<AuthorDTO> authors,
        @JsonProperty("pubdate") String pubDate,
        @JsonProperty("epubdate") String epubDate
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AuthorDTO(String name) {}

    public String getPubMedUrl(){
        return "https://pubmed.ncbi.nlm.nih.gov/" + pmid;
    }
}
