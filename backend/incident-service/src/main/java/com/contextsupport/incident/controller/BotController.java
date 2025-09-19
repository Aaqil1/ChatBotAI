package com.contextsupport.incident.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.contextsupport.incident.dto.BotMessageRequest;
import com.contextsupport.incident.dto.BotMessageResponse;
import com.contextsupport.incident.service.ChatService;

@RestController
@RequestMapping
@Validated
public class BotController {

    private final ChatService chatService;

    public BotController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/botMessage")
    public BotMessageResponse handleBotMessage(@RequestBody @Validated BotMessageRequest request) {
        return chatService.handleMessage(request);
    }
}
