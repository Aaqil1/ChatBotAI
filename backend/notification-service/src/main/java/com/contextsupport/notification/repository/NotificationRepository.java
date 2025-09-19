package com.contextsupport.notification.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.contextsupport.notification.model.AgentNotification;

public interface NotificationRepository extends MongoRepository<AgentNotification, String> {
    List<AgentNotification> findByAcknowledgedFalse();
}
