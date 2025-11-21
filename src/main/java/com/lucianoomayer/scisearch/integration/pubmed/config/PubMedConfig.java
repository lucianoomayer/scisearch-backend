package com.lucianoomayer.scisearch.integration.pubmed.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class PubMedConfig {
    private String PUBMED_API_BASE_URL = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/";

    @Bean
    public WebClient pubMedWebClient(WebClient.Builder builder){
        return builder
                .baseUrl(PUBMED_API_BASE_URL)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .build();
    }
}
