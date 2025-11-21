package com.lucianoomayer.scisearch.integration.crossRef.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class CrossRefConfig {
    private String CROSSREF_API_BASE_URL = "https://api.crossref.org/works";

    @Bean
    public WebClient crossRefWebClient(WebClient.Builder builder){

        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();

        return builder
                .baseUrl(CROSSREF_API_BASE_URL)
                .exchangeStrategies(strategies)
                .defaultHeader(HttpHeaders.USER_AGENT, "scisearch-project/1.0 (mailto:luciano_mayer@hotmail.com)")
                .build();
    }
}
