package com.kafka.message_stream_processor.controller;

import com.kafka.message_stream_processor.dto.MessageRequest;
import com.kafka.message_stream_processor.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller that provides endpoints for publishing messages to Kafka.
 * <p>
 * This controller handles incoming HTTP POST requests and delegates the message publishing
 * logic to the {@link MessageService}.
 * </p>
 */
@RestController
@RequestMapping("/publish")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    /**
     * Service for handling message publishing operations.
     */
    private final MessageService messageService;

    /**
     * Handles HTTP POST requests to publish a message to a Kafka topic.
     * <p>
     * Accepts a JSON payload containing a message and publishes it to the Kafka topic.
     * </p>
     *
     * @param messageRequest the incoming request containing the message.
     * @return ResponseEntity indicating the outcome of the publishing operation.
     */
    @PostMapping
    public ResponseEntity<String> publishMessage(@Valid @RequestBody MessageRequest messageRequest) {
        try {
            messageService.publishMessage(messageRequest.getMessage());
            log.info("Message published successfully: {}", messageRequest.getMessage());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Message published successfully.");
        } catch (Exception e) {
            log.error("Failed to publish message: {}", messageRequest.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to publish message.");
        }
    }
}
