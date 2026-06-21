package com.example.WikiMonitor.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "my_topic", groupId = "my-group")
    public void consumeMessage(String message) {
        System.out.println("Получено сообщение из Kafka: " + message);
    }
}
