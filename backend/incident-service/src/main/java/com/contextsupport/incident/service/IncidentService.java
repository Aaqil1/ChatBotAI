package com.contextsupport.incident.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.contextsupport.incident.dto.IncidentResponse;
import com.contextsupport.incident.dto.MessageDto;
import com.contextsupport.incident.dto.ParseEmailRequest;
import com.contextsupport.incident.exception.ResourceNotFoundException;
import com.contextsupport.incident.model.ChatMessage;
import com.contextsupport.incident.model.Incident;
import com.contextsupport.incident.model.IncidentStatus;
import com.contextsupport.incident.repository.IncidentRepository;
import com.contextsupport.incident.repository.MessageRepository;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final MessageRepository messageRepository;
    private final EmailService emailService;
    private final NotificationPublisher notificationPublisher;

    public IncidentService(IncidentRepository incidentRepository,
                           MessageRepository messageRepository,
                           EmailService emailService,
                           NotificationPublisher notificationPublisher) {
        this.incidentRepository = incidentRepository;
        this.messageRepository = messageRepository;
        this.emailService = emailService;
        this.notificationPublisher = notificationPublisher;
    }

    @Transactional
    public IncidentResponse parseEmail(ParseEmailRequest request) {
        Incident incident = incidentRepository.findById(request.incidentId())
                .orElseGet(() -> incidentRepository.save(Incident.builder()
                        .id(request.incidentId())
                        .email(request.from())
                        .status(IncidentStatus.OPEN)
                        .priority("NORMAL")
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build()));

        emailService.persistEmail(request);
        emailService.sendIncidentLink(incident.getEmail(), incident.getId());

        return mapToResponse(incident.getId());
    }

    public IncidentResponse getIncident(String incidentId) {
        return mapToResponse(incidentId);
    }

    public void escalateIncident(String incidentId, String summary) {
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new ResourceNotFoundException("Incident not found: " + incidentId));
        incident.setStatus(IncidentStatus.ESCALATED);
        incident.setUpdatedAt(Instant.now());
        incidentRepository.save(incident);
        notificationPublisher.publishEscalation(incidentId, summary);
    }

    private IncidentResponse mapToResponse(String incidentId) {
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new ResourceNotFoundException("Incident not found: " + incidentId));
        List<MessageDto> messages = messageRepository.findByIncidentIdOrderByTimestampAsc(incidentId)
                .stream()
                .map(this::mapMessage)
                .toList();
        return new IncidentResponse(
                incident.getId(),
                incident.getEmail(),
                incident.getStatus(),
                incident.getPriority(),
                incident.getCreatedAt(),
                incident.getUpdatedAt(),
                messages
        );
    }

    private MessageDto mapMessage(ChatMessage message) {
        return new MessageDto(
                message.getId(),
                message.getIncidentId(),
                message.getSenderType(),
                message.getMessageBody(),
                message.getSentimentScore(),
                message.getTimestamp()
        );
    }
}
