package com.contextsupport.incident.controller;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.contextsupport.incident.dto.TestEmailRequest;
import com.contextsupport.incident.service.EmailService;

@RestController
@RequestMapping("/test-email")
public class EmailTestController {

    private static final Logger log = LoggerFactory.getLogger(EmailTestController.class);

    private final EmailService emailService;

    public EmailTestController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<Void> sendTestEmail(@RequestBody @Valid TestEmailRequest request) {
        emailService.sendTestEmail(request.from(), request.subject(), request.body());
        log.info("Queued test email from {} with subject '{}'", request.from(), request.subject());
        return ResponseEntity.accepted().build();
    }
}
