package com.contextsupport.sentiment.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.contextsupport.sentiment.dto.AnalyzeMessageRequest;
import com.contextsupport.sentiment.dto.SentimentResponse;
import com.contextsupport.sentiment.service.SentimentService;

@RestController
@RequestMapping
@Validated
public class SentimentController {

    private final SentimentService sentimentService;

    public SentimentController(SentimentService sentimentService) {
        this.sentimentService = sentimentService;
    }

    @PostMapping("/analyzeMessage")
    public SentimentResponse analyzeMessage(@RequestBody @Validated AnalyzeMessageRequest request) {
        return sentimentService.analyze(request);
    }
}
