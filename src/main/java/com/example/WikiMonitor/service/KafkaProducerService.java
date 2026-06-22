package com.example.WikiMonitor.service;

import com.example.WikiMonitor.dto.ChangeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, ChangeEvent> kafkaTemplate;

    public void sendMessage(ChangeEvent event) {
        //kafkaTemplate.send("my_topic", message);
        //System.out.println("Сообщение отправлено в Kafka: " + message);
        kafkaTemplate.send("my_topic", event);
        System.out.println("Сообщение отправлено в Kafka. Название:  " + event.getTitle() + " , тип: " + event.getType());
    }
}
