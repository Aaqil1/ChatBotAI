package com.contextsupport.sentiment.model;

import java.time.Instant;

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
@Document(collection = "sentiments")
public class SentimentRecord {

    @Id
    private String id;
    private String incidentId;
    private String message;
    private double score;
    private Instant createdAt;
}
