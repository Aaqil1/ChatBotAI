package com.contextsupport.incident.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.contextsupport.incident.dto.IncidentResponse;
import com.contextsupport.incident.dto.ParseEmailRequest;
import com.contextsupport.incident.service.IncidentService;

@RestController
@RequestMapping
@Validated
public class IncidentController {

    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @PostMapping("/parseEmail")
    @ResponseStatus(HttpStatus.CREATED)
    public IncidentResponse parseEmail(@RequestBody @Validated ParseEmailRequest request) {
        return incidentService.parseEmail(request);
    }

    @GetMapping("/incident/{id}")
    public IncidentResponse getIncident(@PathVariable("id") String incidentId) {
        return incidentService.getIncident(incidentId);
    }
}
