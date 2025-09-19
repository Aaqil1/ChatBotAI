package com.contextsupport.notification.dto;

import java.util.List;

public record AgentProfileResponse(
        String id,
        String name,
        String email,
        List<String> assignedIncidents
) {
}
