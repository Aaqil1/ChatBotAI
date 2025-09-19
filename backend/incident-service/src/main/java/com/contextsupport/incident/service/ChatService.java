package com.contextsupport.incident.service;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.contextsupport.incident.dto.BotMessageRequest;
import com.contextsupport.incident.dto.BotMessageResponse;
import com.contextsupport.incident.dto.MessageDto;
import com.contextsupport.incident.model.ChatMessage;
import com.contextsupport.incident.repository.MessageRepository;

@Service
public class ChatService {

    private static final Logger log = LoggerFactory.getLogger(ChatService.class);
    private static final double ESCALATION_THRESHOLD = -0.5;

    private final MessageRepository messageRepository;
    private final SentimentClient sentimentClient;
    private final BotIntegrationService botIntegrationService;
    private final IncidentService incidentService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatService(MessageRepository messageRepository,
                       SentimentClient sentimentClient,
                       BotIntegrationService botIntegrationService,
                       IncidentService incidentService,
                       SimpMessagingTemplate messagingTemplate) {
        this.messageRepository = messageRepository;
        this.sentimentClient = sentimentClient;
        this.botIntegrationService = botIntegrationService;
        this.incidentService = incidentService;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    public BotMessageResponse handleMessage(BotMessageRequest request) {
        double sentiment = sentimentClient.analyze(request.incidentId(), request.messageBody());
        ChatMessage saved = persistAndBroadcast(request.incidentId(), request.senderType(), request.messageBody(), sentiment);

        if (sentiment <= ESCALATION_THRESHOLD && "USER".equalsIgnoreCase(request.senderType())) {
            log.info("Triggering escalation due to negative sentiment: {}", sentiment);
            incidentService.escalateIncident(request.incidentId(), "Detected negative sentiment from user");
        }

        if ("USER".equalsIgnoreCase(request.senderType())) {
            String botReply = botIntegrationService.fetchBotReply(request.incidentId(), request.messageBody());
            double botSentiment = sentimentClient.analyze(request.incidentId(), botReply);
            persistAndBroadcast(request.incidentId(), "BOT", botReply, botSentiment);
            return new BotMessageResponse(request.incidentId(), botReply, Instant.now());
        }

        return new BotMessageResponse(request.incidentId(), saved.getMessageBody(), saved.getTimestamp());
    }

    public void handleRealtimeMessage(String incidentId, String senderType, String messageBody) {
        double sentiment = sentimentClient.analyze(incidentId, messageBody);
        persistAndBroadcast(incidentId, senderType, messageBody, sentiment);
    }

    private ChatMessage persistAndBroadcast(String incidentId, String senderType, String messageBody, Double sentiment) {
        ChatMessage message = ChatMessage.builder()
                .incidentId(incidentId)
                .senderType(senderType)
                .messageBody(messageBody)
                .sentimentScore(sentiment)
                .timestamp(Instant.now())
                .build();
        ChatMessage saved = messageRepository.save(message);
        MessageDto payload = new MessageDto(saved.getId(), saved.getIncidentId(), saved.getSenderType(), saved.getMessageBody(), saved.getSentimentScore(), saved.getTimestamp());
        messagingTemplate.convertAndSend("/topic/incident/" + incidentId, payload);
        return saved;
    }
}
