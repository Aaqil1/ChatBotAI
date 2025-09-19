package com.contextsupport.incident.dto;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ParseEmailRequest(
        @NotBlank String incidentId,
        @NotBlank String customerDetails,
        @NotBlank String subject,
        @NotBlank String body,
        List<String> attachments,
        @Email @NotBlank String from
) {
}
