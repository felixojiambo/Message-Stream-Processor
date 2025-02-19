package com.kafka.message_stream_processor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private static final String TOPIC = "message_topic";
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message) {
        try {
            kafkaTemplate.send(TOPIC, message);
            log.info("Message '{}' sent to Kafka topic '{}'", message, TOPIC);
        } catch (Exception e) {
            log.error("Error sending message '{}' to Kafka topic '{}'", message, TOPIC, e);
        }
    }
}
