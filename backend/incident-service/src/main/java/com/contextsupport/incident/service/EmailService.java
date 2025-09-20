package com.contextsupport.incident.service;

import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.contextsupport.incident.dto.ParseEmailRequest;
import com.contextsupport.incident.config.EmailProperties;
import com.contextsupport.incident.model.EmailRecord;
import com.contextsupport.incident.repository.EmailRepository;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final EmailRepository emailRepository;
    private final JavaMailSender mailSender;
    private final EmailProperties properties;

    public EmailService(EmailRepository emailRepository,
                        JavaMailSender mailSender,
                        EmailProperties properties) {
        this.emailRepository = emailRepository;
        this.mailSender = mailSender;
        this.properties = properties;
    }

    public EmailRecord persistEmail(ParseEmailRequest request) {
        EmailRecord record = EmailRecord.builder()
                .incidentId(request.incidentId())
                .customerDetails(request.customerDetails())
                .subject(request.subject())
                .body(request.body())
                .attachments(request.attachments() == null ? List.of() : request.attachments())
                .from(request.from())
                .receivedAt(Instant.now())
                .build();
        return emailRepository.save(record);
    }

    public void sendIncidentLink(String email, String incidentId) {
        if (!properties.getSmtp().isEnabled()) {
            log.info("SMTP disabled; skipping incident link email for {}", incidentId);
            return;
        }
        String portalLink = properties.getPortalBaseUrl() + "/user?incidentId=" + incidentId;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom(properties.getSmtp().getFromAddress());
        message.setSubject("Incident " + incidentId + " created");
        message.setText("Hi,\n\nWe've created incident " + incidentId + " for your request. "
                + "You can jump straight into the chat here: " + portalLink + "\n\nRegards,\nSupport Bot");
        try {
            mailSender.send(message);
        } catch (MailException ex) {
            log.error("Failed to send incident link email for {}", incidentId, ex);
        }
    }

    public void sendTestEmail(String from, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(properties.getInbox().getAddress());
        message.setFrom(from);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
