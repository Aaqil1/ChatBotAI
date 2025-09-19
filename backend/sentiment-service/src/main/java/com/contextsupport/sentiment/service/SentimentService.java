package com.contextsupport.sentiment.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.contextsupport.sentiment.dto.AnalyzeMessageRequest;
import com.contextsupport.sentiment.dto.SentimentResponse;
import com.contextsupport.sentiment.model.SentimentRecord;
import com.contextsupport.sentiment.repository.SentimentRepository;

@Service
public class SentimentService {

    private final SentimentAnalyzer sentimentAnalyzer;
    private final SentimentRepository sentimentRepository;

    public SentimentService(SentimentAnalyzer sentimentAnalyzer, SentimentRepository sentimentRepository) {
        this.sentimentAnalyzer = sentimentAnalyzer;
        this.sentimentRepository = sentimentRepository;
    }

    public SentimentResponse analyze(AnalyzeMessageRequest request) {
        List<String> messages = new ArrayList<>();
        if (request.messages() != null && !request.messages().isEmpty()) {
            messages.addAll(request.messages());
        }
        if (request.message() != null && !request.message().isBlank()) {
            messages.add(request.message());
        }
        if (messages.isEmpty()) {
            messages.add("neutral");
        }
        double average = messages.stream()
                .mapToDouble(sentimentAnalyzer::score)
                .average()
                .orElse(0.0);
        SentimentRecord record = SentimentRecord.builder()
                .incidentId(request.incidentId())
                .message(String.join("\n", messages))
                .score(average)
                .createdAt(Instant.now())
                .build();
        SentimentRecord saved = sentimentRepository.save(record);
        return new SentimentResponse(saved.getIncidentId(), saved.getScore(), saved.getCreatedAt(), "InHouseLexicon");
    }
}
