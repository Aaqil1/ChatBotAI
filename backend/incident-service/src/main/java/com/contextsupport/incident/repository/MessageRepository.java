package com.contextsupport.incident.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.contextsupport.incident.model.ChatMessage;

public interface MessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findByIncidentIdOrderByTimestampAsc(String incidentId);
}
