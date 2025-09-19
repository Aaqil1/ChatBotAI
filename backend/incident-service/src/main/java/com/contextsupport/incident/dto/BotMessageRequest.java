package com.contextsupport.incident.dto;

import jakarta.validation.constraints.NotBlank;

public record BotMessageRequest(
        @NotBlank String incidentId,
        @NotBlank String senderType,
        @NotBlank String messageBody
) {
}
