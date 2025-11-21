package com.lucianoomayer.scisearch.integration.pubmed.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.util.*;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetaDataResultDTO {

    private Result result;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {

        private List<String> uids;

        @JsonIgnore
        private Map<String, PubMedArticleDTO> articles = new HashMap<>();

        @JsonAnySetter
        public void addArticle(String key, Object value) {
            if (!"uids".equals(key)) {
                PubMedArticleDTO article = new ObjectMapper().convertValue(value, PubMedArticleDTO.class);
                articles.put(key, article);
            }
        }

        public List<PubMedArticleDTO> getArticlesList() {
            return new ArrayList<>(articles.values());
        }
    }
}
