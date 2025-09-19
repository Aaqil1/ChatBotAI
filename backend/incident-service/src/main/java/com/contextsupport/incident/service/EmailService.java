package com.contextsupport.incident.service;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.contextsupport.incident.dto.ParseEmailRequest;
import com.contextsupport.incident.model.EmailRecord;
import com.contextsupport.incident.repository.EmailRepository;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final EmailRepository emailRepository;

    public EmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    public EmailRecord persistEmail(ParseEmailRequest request) {
        EmailRecord record = EmailRecord.builder()
                .incidentId(request.incidentId())
                .customerDetails(request.customerDetails())
                .subject(request.subject())
                .body(request.body())
                .attachments(request.attachments())
                .from(request.from())
                .receivedAt(Instant.now())
                .build();
        return emailRepository.save(record);
    }

    public void sendIncidentLink(String email, String incidentId) {
        // Placeholder for integration with email provider
        log.info("Sending incident link email to {} for incident {}", email, incidentId);
    }
}
