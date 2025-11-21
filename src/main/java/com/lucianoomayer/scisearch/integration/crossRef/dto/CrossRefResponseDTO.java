package com.lucianoomayer.scisearch.integration.crossRef.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CrossRefResponseDTO(Message message) {

    public record Message(List<CrossRefArticleDTO> items) {}
}
