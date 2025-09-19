package com.contextsupport.incident.dto;

import java.time.Instant;
import java.util.List;

import com.contextsupport.incident.model.IncidentStatus;

public record IncidentResponse(
        String id,
        String email,
        IncidentStatus status,
        String priority,
        Instant createdAt,
        Instant updatedAt,
        List<MessageDto> messages
) {
}
