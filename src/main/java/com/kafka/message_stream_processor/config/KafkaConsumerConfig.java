package com.kafka.message_stream_processor.config;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;

/**
 * Configuration class for Kafka consumer error handling.
 * This class sets up a custom error handler with exponential backoff and dead-letter topic recovery.
 */
@Configuration
public class KafkaConsumerConfig {

    /**
     * KafkaTemplate for sending messages to a dead-letter topic.
     * Automatically injected by Spring.
     */
    @Autowired
    private KafkaTemplate<String, String> deadLetterKafkaTemplate;

    /**
     * Defines a custom error handler bean for Kafka consumers.
     * <p>
     * - Implements an exponential backoff retry strategy with a maximum of 3 retries.
     * - On failure after retries, sends the message to a dead-letter topic ("dead_letter_topic").
     * </p>
     *
     * @return DefaultErrorHandler configured with backoff and dead-letter recovery.
     */
    @Bean
    public DefaultErrorHandler kafkaErrorHandler() {
        // Configure exponential backoff: 3 retries, starting at 1000ms, doubling each time, max interval 5000ms.
        ExponentialBackOffWithMaxRetries backOff = new ExponentialBackOffWithMaxRetries(3);
        backOff.setInitialInterval(1000L);
        backOff.setMultiplier(2.0);
        backOff.setMaxInterval(5000L);

        // Recovery logic: On failure after retries, send the record to the dead-letter topic.
        ConsumerRecordRecoverer recoverer = (ConsumerRecord<?, ?> record, Exception exception) -> {
            String key = record.key() != null ? record.key().toString() : null;
            String value = record.value() != null ? record.value().toString() : null;

            // Ensure key is not null (optional: you can handle null keys differently if needed).
            assert key != null;

            // Send the failed message to the dead-letter topic.
            deadLetterKafkaTemplate.send("dead_letter_topic", key, value);

            // Log the recovery action.
            System.err.println("Sent record to dead-letter topic. Key: " + key + ", Value: " + value);
        };

        // Return the error handler with the configured backoff and recoverer.
        return new DefaultErrorHandler(recoverer, backOff);
    }
}
