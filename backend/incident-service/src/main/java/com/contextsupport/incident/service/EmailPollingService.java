package com.contextsupport.incident.service;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import jakarta.mail.Address;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.contextsupport.incident.config.EmailProperties;
import com.contextsupport.incident.config.FakeSmtpServer;
import com.contextsupport.incident.dto.ParseEmailRequest;

import org.subethamail.smtp.server.WiserMessage;

@Service
public class EmailPollingService {

    private static final Logger log = LoggerFactory.getLogger(EmailPollingService.class);

    private final FakeSmtpServer fakeSmtpServer;
    private final IncidentService incidentService;
    private final EmailProperties properties;

    public EmailPollingService(FakeSmtpServer fakeSmtpServer,
                               IncidentService incidentService,
                               EmailProperties properties) {
        this.fakeSmtpServer = fakeSmtpServer;
        this.incidentService = incidentService;
        this.properties = properties;
    }

    @Scheduled(fixedDelayString = "${email.inbox.polling-interval-ms:5000}")
    public void pollInbox() {
        if (!properties.getInbox().isEnabled()) {
            return;
        }
        if (!fakeSmtpServer.isRunning()) {
            return;
        }
        List<WiserMessage> messages = fakeSmtpServer.drainMessages();
        if (messages.isEmpty()) {
            return;
        }
        for (WiserMessage wiserMessage : messages) {
            String recipient = wiserMessage.getEnvelopeReceiver();
            if (recipient != null && !recipient.equalsIgnoreCase(properties.getInbox().getAddress())) {
                log.debug("Ignoring message for non-inbox recipient {}", recipient);
                continue;
            }
            try {
                processMessage(wiserMessage.getMimeMessage());
            } catch (MessagingException | IOException ex) {
                log.error("Failed to process inbound email", ex);
            }
        }
    }

    private void processMessage(MimeMessage message) throws MessagingException, IOException {
        String messageId = message.getMessageID();
        Address[] from = message.getFrom();
        if (from == null || from.length == 0) {
            log.warn("Skipping email without sender, messageId={}", messageId);
            return;
        }
        String sender = ((InternetAddress) from[0]).getAddress();
        String customerDetails = resolveCustomerDetails((InternetAddress) from[0]);
        String subject = message.getSubject();
        String body = extractBody(message);

        String incidentId = generateIncidentId();
        log.info("Creating incident {} from inbound email {}, subject='{}'", incidentId, sender, subject);

        incidentService.parseEmail(new ParseEmailRequest(
                incidentId,
                customerDetails,
                StringUtils.hasText(subject) ? subject : "(no subject)",
                body,
                List.of(),
                sender
        ));
    }

    private String extractBody(Message message) throws MessagingException, IOException {
        Object content = message.getContent();
        if (content instanceof String text) {
            return text;
        }
        if (content instanceof MimeMultipart multipart) {
            return extractFromMultipart(multipart);
        }
        return "";
    }

    private String extractFromMultipart(MimeMultipart multipart) throws MessagingException, IOException {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < multipart.getCount(); i++) {
            var bodyPart = multipart.getBodyPart(i);
            Object partContent = bodyPart.getContent();
            if (partContent instanceof String text) {
                builder.append(text);
            }
        }
        return builder.toString();
    }

    private String resolveCustomerDetails(InternetAddress address) {
        if (StringUtils.hasText(address.getPersonal())) {
            return address.getPersonal();
        }
        return address.getAddress();
    }

    private String generateIncidentId() {
        return "INC-" + Instant.now().toEpochMilli() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.US);
    }
}
