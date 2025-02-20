package com.kafka.message_stream_processor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;

/**
 * Configuration class for Kafka listener container factory.
 * <p>
 * This configuration sets up the `ConcurrentKafkaListenerContainerFactory` with a custom error handler.
 * The error handler is defined in {@link KafkaConsumerConfig}.
 * </p>
 */
@Configuration
public class KafkaListenerConfig {

    /**
     * Consumer factory responsible for creating Kafka consumers.
     */
    private final ConsumerFactory<String, String> consumerFactory;

    /**
     * Custom error handler for Kafka listeners, handling retries and dead-letter processing.
     */
    private final DefaultErrorHandler kafkaErrorHandler;

    /**
     * Constructor-based dependency injection for consumer factory and error handler.
     *
     * @param consumerFactory   Factory for creating Kafka consumers.
     * @param kafkaErrorHandler Custom error handler for Kafka listeners.
     */
    public KafkaListenerConfig(ConsumerFactory<String, String> consumerFactory, DefaultErrorHandler kafkaErrorHandler) {
        this.consumerFactory = consumerFactory;
        this.kafkaErrorHandler = kafkaErrorHandler;
    }

    /**
     * Configures and provides a `ConcurrentKafkaListenerContainerFactory` bean.
     * <p>
     * This factory is used by all `@KafkaListener` methods in the application.
     * It is configured with:
     * - The provided `ConsumerFactory` to create consumer instances.
     * - The custom `DefaultErrorHandler` for error handling and retries.
     * </p>
     *
     * @return Configured `ConcurrentKafkaListenerContainerFactory` bean.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(kafkaErrorHandler);
        return factory;
    }
}
