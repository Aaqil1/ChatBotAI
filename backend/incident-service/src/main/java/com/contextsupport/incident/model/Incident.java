package com.contextsupport.incident.model;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
@Document(collection = "incidents")
public class Incident {

    @Id
    private String id;
    private String email;
    private IncidentStatus status;
    private String priority;
    private Instant createdAt;
    private Instant updatedAt;
    @Singular
    private List<String> assignedAgents;
}
