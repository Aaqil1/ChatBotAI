package com.contextsupport.notification.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor(onConstructor = @__(@PersistenceConstructor))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "agents")
public class Agent {

    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private List<String> assignedIncidents;
}
