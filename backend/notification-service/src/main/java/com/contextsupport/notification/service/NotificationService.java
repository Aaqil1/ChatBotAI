package com.contextsupport.notification.service;

import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.contextsupport.notification.dto.CreateNotificationRequest;
import com.contextsupport.notification.dto.NotificationResponse;
import com.contextsupport.notification.exception.ResourceNotFoundException;
import com.contextsupport.notification.model.AgentNotification;
import com.contextsupport.notification.repository.NotificationRepository;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<NotificationResponse> getPendingNotifications() {
        return notificationRepository.findByAcknowledgedFalse().stream()
                .map(this::map)
                .toList();
    }

    @Transactional
    public NotificationResponse createNotification(CreateNotificationRequest request) {
        AgentNotification notification = AgentNotification.builder()
                .incidentId(request.incidentId())
                .summary(request.summary())
                .type(request.type())
                .acknowledged(false)
                .createdAt(Instant.now())
                .build();
        AgentNotification saved = notificationRepository.save(notification);
        log.info("Persisted notification for incident {}", saved.getIncidentId());
        return map(saved);
    }

    @Transactional
    public NotificationResponse acknowledge(String notificationId) {
        AgentNotification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        notification.setAcknowledged(true);
        notification.setAcknowledgedAt(Instant.now());
        return map(notificationRepository.save(notification));
    }

    private NotificationResponse map(AgentNotification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getIncidentId(),
                notification.getSummary(),
                notification.getType(),
                notification.isAcknowledged(),
                notification.getCreatedAt(),
                notification.getAcknowledgedAt()
        );
    }
}
