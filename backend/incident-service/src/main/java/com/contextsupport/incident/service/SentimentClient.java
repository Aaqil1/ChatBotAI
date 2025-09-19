package com.contextsupport.incident.service;

import java.time.Duration;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class SentimentClient {

    private static final Logger log = LoggerFactory.getLogger(SentimentClient.class);

    private final RestClient restClient;
    private final String analyzePath;

    public SentimentClient(@Value("${services.sentiment.base-url:http://localhost:8082}") String baseUrl,
                           @Value("${services.sentiment.path:/analyzeMessage}") String analyzePath) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
        this.analyzePath = analyzePath;
    }

    public double analyze(String incidentId, String message) {
        try {
            Map<String, Object> response = restClient.post()
                    .uri(analyzePath)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("incidentId", incidentId, "message", message))
                    .retrieve()
                    .body(Map.class);
            if (response != null && response.get("score") instanceof Number score) {
                return score.doubleValue();
            }
        } catch (Exception ex) {
            log.warn("Sentiment analysis service unavailable, defaulting neutral score. {}", ex.getMessage());
        }
        return 0.0;
    }
}
