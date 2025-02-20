package com.kafka.message_stream_processor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for producing (publishing) messages to a Kafka topic.
 * <p>
 * This service publishes messages to the "message_topic" Kafka topic using a {@link KafkaTemplate}.
 * It handles any errors that may occur during the sending process and logs both success and failure events.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    /**
     * Kafka topic to which messages will be published.
     */
    private static final String TOPIC = "message_topic";

    /**
     * KafkaTemplate for publishing messages to Kafka.
     */
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Sends a message to the configured Kafka topic ("message_topic").
     * <p>
     * Logs the message upon successful sending or logs an error if sending fails.
     * </p>
     *
     * @param message the message payload to be sent to Kafka.
     */
    public void sendMessage(String message) {
        try {
            kafkaTemplate.send(TOPIC, message);
            log.info("Message '{}' sent to Kafka topic '{}'", message, TOPIC);
        } catch (Exception e) {
            log.error("Error sending message '{}' to Kafka topic '{}'", message, TOPIC, e);
        }
    }
}
