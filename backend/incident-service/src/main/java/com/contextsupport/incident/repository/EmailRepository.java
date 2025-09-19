package com.contextsupport.incident.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.contextsupport.incident.model.EmailRecord;

public interface EmailRepository extends MongoRepository<EmailRecord, String> {
    Optional<EmailRecord> findByIncidentId(String incidentId);
}
