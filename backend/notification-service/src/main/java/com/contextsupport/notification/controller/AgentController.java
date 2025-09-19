package com.contextsupport.notification.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.contextsupport.notification.dto.AgentProfileResponse;
import com.contextsupport.notification.service.AgentService;

@RestController
@RequestMapping
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @GetMapping("/me")
    public AgentProfileResponse getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return agentService.getProfile(userDetails.getUsername());
    }
}
