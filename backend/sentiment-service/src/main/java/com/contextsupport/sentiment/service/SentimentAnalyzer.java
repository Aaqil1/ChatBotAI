package com.contextsupport.sentiment.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class SentimentAnalyzer {

    private static final Set<String> POSITIVE = new HashSet<>(Arrays.asList("great", "good", "awesome", "thanks", "happy", "resolved"));
    private static final Set<String> NEGATIVE = new HashSet<>(Arrays.asList("bad", "angry", "upset", "terrible", "frustrated", "unhappy", "issue", "problem"));

    public double score(String message) {
        if (message == null || message.isBlank()) {
            return 0.0;
        }
        String normalized = message.toLowerCase(Locale.ENGLISH);
        long pos = POSITIVE.stream().filter(normalized::contains).count();
        long neg = NEGATIVE.stream().filter(normalized::contains).count();
        if (pos == 0 && neg == 0) {
            return 0.0;
        }
        double total = pos + neg;
        return (pos - neg) / total;
    }
}
