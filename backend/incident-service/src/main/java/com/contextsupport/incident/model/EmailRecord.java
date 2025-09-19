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

@Data
@Builder
@AllArgsConstructor(onConstructor = @__(@PersistenceConstructor))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
