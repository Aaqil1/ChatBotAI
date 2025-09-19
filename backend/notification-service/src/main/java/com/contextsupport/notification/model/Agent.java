package com.contextsupport.notification.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collection = "agents")
public class Agent {

    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private List<String> assignedIncidents;
}
