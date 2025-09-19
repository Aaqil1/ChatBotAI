package com.contextsupport.sentiment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class SentimentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SentimentServiceApplication.class, args);
    }
}
