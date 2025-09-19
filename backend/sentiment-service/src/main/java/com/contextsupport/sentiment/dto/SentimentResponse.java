package com.contextsupport.sentiment.dto;

import java.time.Instant;

public record SentimentResponse(
        String incidentId,
        double score,
        Instant createdAt,
        String provider
) {
}
