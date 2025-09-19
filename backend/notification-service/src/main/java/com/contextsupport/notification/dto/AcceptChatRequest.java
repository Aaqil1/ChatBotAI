package com.contextsupport.notification.dto;

import jakarta.validation.constraints.NotBlank;

public record AcceptChatRequest(
        @NotBlank String incidentId
) {
}
