package com.contextsupport.incident.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.contextsupport.incident.model.Incident;

public interface IncidentRepository extends MongoRepository<Incident, String> {
}
