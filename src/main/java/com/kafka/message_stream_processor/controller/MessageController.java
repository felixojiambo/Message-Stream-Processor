package com.kafka.message_stream_processor.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/publish")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private static final String TOPIC = "message_topic";
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * POST endpoint to publish a message to Kafka.
     *
     * @param messageRequest the request containing the message payload.
     * @return ResponseEntity with the operation result.
     */
    @PostMapping
    public ResponseEntity<String> publishMessage(@RequestBody MessageRequest messageRequest) {
        try {
            kafkaTemplate.send(TOPIC, messageRequest.getMessage());
            log.info("Published message: '{}' to Kafka topic: '{}'", messageRequest.getMessage(), TOPIC);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Message published successfully.");
        } catch (Exception e) {
            log.error("Error while publishing message to Kafka", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to publish message.");
        }
    }
}
