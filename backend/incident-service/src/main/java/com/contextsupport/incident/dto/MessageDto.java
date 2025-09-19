package com.contextsupport.incident.dto;

import java.time.Instant;

public record MessageDto(
        String id,
        String incidentId,
        String senderType,
        String messageBody,
        Double sentimentScore,
        Instant timestamp
) {
}
