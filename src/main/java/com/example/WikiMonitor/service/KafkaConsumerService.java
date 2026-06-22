package com.example.WikiMonitor.service;

import com.example.WikiMonitor.dto.ChangeEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "my_topic", groupId = "my-group")
    public void consumeMessage(ChangeEvent message) {
        System.out.println("Получено сообщение из Kafka. Название:  " + message.getTitle() + " , тип: " + message.getType());
    }
}
