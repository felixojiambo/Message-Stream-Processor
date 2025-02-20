package com.kafka.message_stream_processor.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for consuming messages from Kafka topics.
 * <p>
 * This service listens to messages from the "message_topic" Kafka topic
 * and logs the consumed messages.
 * </p>
 */
@Service
@Slf4j
public class KafkaConsumerService {

    /**
     * Kafka listener method that consumes messages from the "message_topic".
     * <p>
     * The listener is part of the consumer group "group_id". Each incoming message
     * is logged upon successful consumption.
     * </p>
     *
     * @param message the message payload received from the Kafka topic.
     */
    @KafkaListener(topics = "message_topic", groupId = "group_id")
    public void consume(String message) {
        log.info("Consumed message from Kafka: {}", message);
    }
}
