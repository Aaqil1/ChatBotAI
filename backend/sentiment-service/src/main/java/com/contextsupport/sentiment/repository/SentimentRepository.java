package com.contextsupport.sentiment.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.contextsupport.sentiment.model.SentimentRecord;

public interface SentimentRepository extends MongoRepository<SentimentRecord, String> {
    List<SentimentRecord> findTop5ByIncidentIdOrderByCreatedAtDesc(String incidentId);
}
