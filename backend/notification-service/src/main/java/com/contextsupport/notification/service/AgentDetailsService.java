package com.contextsupport.notification.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.contextsupport.notification.repository.AgentRepository;

@Service
public class AgentDetailsService implements UserDetailsService {

    private final AgentRepository agentRepository;

    public AgentDetailsService(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return agentRepository.findByEmail(username)
                .map(agent -> User.withUsername(agent.getEmail())
                        .password(agent.getPassword())
                        .roles("AGENT")
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("Agent not found: " + username));
    }
}
