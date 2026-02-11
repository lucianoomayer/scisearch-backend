package com.lucianoomayer.scisearch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ArticleRequestDTO(
        @NotBlank String externalId,
        @NotBlank String title,
        @NotBlank String url,
        @NotNull Integer publicationYear,
        @NotBlank String source
) {}
