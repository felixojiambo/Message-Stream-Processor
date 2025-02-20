package com.kafka.message_stream_processor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private static final String TOPIC = "message_topic";
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Publishes a message to the Kafka topic asynchronously.
     *
     * @param message the message payload to publish.
     */
    public void publishMessage(final String message) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC, message);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Error publishing message to topic {}: {}", TOPIC, message, ex);
            } else {
                log.info("Message successfully sent to topic {}: {}", TOPIC, message);
            }
        });
    }
}
