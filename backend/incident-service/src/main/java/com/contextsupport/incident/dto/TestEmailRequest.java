package com.contextsupport.incident.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record TestEmailRequest(
        @Email @NotBlank String from,
        @NotBlank String subject,
        @NotBlank String body
) {
}
