package com.contextsupport.notification.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.contextsupport.notification.dto.AcceptChatRequest;
import com.contextsupport.notification.service.AgentService;
import com.contextsupport.notification.service.NotificationService;

@RestController
@RequestMapping
@Validated
public class AcceptChatController {

    private final AgentService agentService;
    private final NotificationService notificationService;

    public AcceptChatController(AgentService agentService, NotificationService notificationService) {
        this.agentService = agentService;
        this.notificationService = notificationService;
    }

    @PostMapping("/acceptChat")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void acceptChat(@AuthenticationPrincipal UserDetails userDetails, @RequestBody @Validated AcceptChatRequest request) {
        agentService.assignIncident(userDetails.getUsername(), request.incidentId());
        notificationService.getPendingNotifications().stream()
                .filter(n -> n.incidentId().equalsIgnoreCase(request.incidentId()))
                .findFirst()
                .ifPresent(n -> notificationService.acknowledge(n.id()));
    }
}
