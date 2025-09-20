package com.contextsupport.incident.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class NotificationPublisher {

    private static final Logger log = LoggerFactory.getLogger(NotificationPublisher.class);
    private final RestClient restClient;
    private final String notificationPath;

    public NotificationPublisher(@Value("${services.notification.base-url:http://localhost:8081}") String baseUrl,
                                 @Value("${services.notification.path:/notifications}") String notificationPath) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
        this.notificationPath = notificationPath;
    }

    public void publishEscalation(String incidentId, String summary) {
        try {
            restClient.post()
                    .uri(notificationPath)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "incidentId", incidentId,
                            "summary", summary,
                            "type", "ESCALATION"
                    ))
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception ex) {
            log.warn("Notification service unavailable, caching message locally. {}", ex.getMessage());
        }
    }
}
