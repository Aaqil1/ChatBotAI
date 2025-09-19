package com.contextsupport.incident.model;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Data
@Builder
@AllArgsConstructor(onConstructor = @__(@PersistenceConstructor))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
