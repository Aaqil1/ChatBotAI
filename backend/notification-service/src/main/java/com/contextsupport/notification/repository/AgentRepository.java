package com.contextsupport.notification.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.contextsupport.notification.model.Agent;

public interface AgentRepository extends MongoRepository<Agent, String> {
    Optional<Agent> findByEmail(String email);
}
