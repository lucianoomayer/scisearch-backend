package com.lucianoomayer.scisearch.integration.pubmed.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PmidSearchResultDTO(@JsonProperty("esearchresult") PmidResultDTO searchResult ) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PmidResultDTO(
            @JsonProperty("count") int count,
            @JsonProperty("idlist") List<String> pmid
    ) {}
}
