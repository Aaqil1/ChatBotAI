package com.contextsupport.incident.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import com.contextsupport.incident.dto.MessageDto;
import com.contextsupport.incident.service.ChatService;

@Controller
public class ChatSocketController {

    private final ChatService chatService;

    public ChatSocketController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/incident/{incidentId}")
    public void relayMessage(@DestinationVariable String incidentId, @Payload MessageDto message) {
        chatService.handleRealtimeMessage(incidentId, message.senderType(), message.messageBody());
    }
}
