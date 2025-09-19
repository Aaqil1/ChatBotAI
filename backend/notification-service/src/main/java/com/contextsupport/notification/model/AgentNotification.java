package com.contextsupport.notification.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collection = "notifications")
public class AgentNotification {

    @Id
    private String id;
    private String incidentId;
    private String summary;
    private String type;
    private boolean acknowledged;
    private Instant createdAt;
    private Instant acknowledgedAt;
}
