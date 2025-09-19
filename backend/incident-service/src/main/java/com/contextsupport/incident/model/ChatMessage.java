package com.contextsupport.incident.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collection = "messages")
public class ChatMessage {

    @Id
    private String id;
    private String incidentId;
    private String senderType;
    private String messageBody;
    private Double sentimentScore;
    private Instant timestamp;
}
