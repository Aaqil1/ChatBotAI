package com.contextsupport.notification.service;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.contextsupport.notification.dto.AgentProfileResponse;
import com.contextsupport.notification.exception.ResourceNotFoundException;
import com.contextsupport.notification.model.Agent;
import com.contextsupport.notification.repository.AgentRepository;

@Service
public class AgentService implements CommandLineRunner {

    private final AgentRepository agentRepository;
    private final PasswordEncoder passwordEncoder;

    public AgentService(AgentRepository agentRepository, PasswordEncoder passwordEncoder) {
        this.agentRepository = agentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AgentProfileResponse getProfile(String email) {
        Agent agent = agentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Agent not found: " + email));
        return new AgentProfileResponse(agent.getId(), agent.getName(), agent.getEmail(), agent.getAssignedIncidents());
    }

    public void assignIncident(String email, String incidentId) {
        Agent agent = agentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Agent not found: " + email));
        List<String> assigned = agent.getAssignedIncidents();
        if (assigned == null || assigned.stream().noneMatch(id -> id.equalsIgnoreCase(incidentId))) {
            assigned = assigned == null ? new java.util.ArrayList<>() : assigned;
            assigned.add(incidentId);
            agent.setAssignedIncidents(assigned);
            agentRepository.save(agent);
        }
    }

    @Override
    public void run(String... args) {
        if (agentRepository.count() == 0) {
            Agent agent = Agent.builder()
                    .name("Default Agent")
                    .email("agent@example.com")
                    .password(passwordEncoder.encode("password"))
                    .assignedIncidents(new java.util.ArrayList<>())
                    .build();
            agentRepository.save(agent);
        }
    }
}
