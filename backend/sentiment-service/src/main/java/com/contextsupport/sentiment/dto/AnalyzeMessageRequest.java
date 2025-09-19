package com.contextsupport.sentiment.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public record AnalyzeMessageRequest(
        @NotBlank String incidentId,
        String message,
        List<String> messages
) {
}
