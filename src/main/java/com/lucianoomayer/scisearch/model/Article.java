package com.lucianoomayer.scisearch.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Article {
    private String title;
    private String articleId;
    private String articleUrl;
    @JsonProperty("authors")
    private List<String> authors;
    private String abstractText;
    private String publicationDate;
    private String source;
}
