package com.contextsupport.incident.model;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collection = "emails")
public class EmailRecord {

    @Id
    private String id;
    private String incidentId;
    private String customerDetails;
    private String subject;
    private String body;
    private List<String> attachments;
    private String from;
    private Instant receivedAt;
}
