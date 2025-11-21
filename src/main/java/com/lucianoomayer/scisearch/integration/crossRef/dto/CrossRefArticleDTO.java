package com.lucianoomayer.scisearch.integration.crossRef.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class CrossRefArticleDTO {
    @JsonProperty("title")
    private List<String> title;

    private List<Author> author;

    @JsonProperty("published-online")
    private PublishDate publishedOnline;

    @JsonProperty("published-print")
    private PublishDate publishedPrint;

    @JsonProperty("DOI")
    private String articleId;

    @JsonProperty("URL")
    private String url;

    @Data
    public static class Author{
        private String given;
        private String family;

        public String getFullName() {
            return ((given != null ? given : "") + " " + (family != null ? family : "")).trim();
        }
    }

    @Data
    public static class PublishDate {
        @JsonProperty("date-parts")
        private List<List<Integer>> dateParts;

        public String extractYear(){
            return String.valueOf(dateParts.get(0).get(0));
        }
    }
}
