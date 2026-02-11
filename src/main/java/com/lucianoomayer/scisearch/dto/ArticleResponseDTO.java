package com.lucianoomayer.scisearch.dto;

import java.util.List;

public record ArticleResponseDTO(
        String externalId,
        String title,
        String url,
        List<String> authors,
        Integer publicationYear,
        String source
) {}
