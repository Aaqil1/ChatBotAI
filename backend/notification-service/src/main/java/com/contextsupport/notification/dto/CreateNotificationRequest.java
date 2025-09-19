package com.contextsupport.notification.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateNotificationRequest(
        @NotBlank String incidentId,
        @NotBlank String summary,
        @NotBlank String type
) {
}
