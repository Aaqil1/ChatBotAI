package com.contextsupport.incident.dto;

import java.time.Instant;

public record BotMessageResponse(
        String incidentId,
        String botMessage,
        Instant timestamp
) {
}
