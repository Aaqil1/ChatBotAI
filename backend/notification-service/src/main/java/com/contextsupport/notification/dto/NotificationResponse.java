package com.contextsupport.notification.dto;

import java.time.Instant;

public record NotificationResponse(
        String id,
        String incidentId,
        String summary,
        String type,
        boolean acknowledged,
        Instant createdAt,
        Instant acknowledgedAt
) {
}
