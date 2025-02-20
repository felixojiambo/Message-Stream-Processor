package com.kafka.message_stream_processor.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Data Transfer Object (DTO) representing a message request payload.
 * <p>
 * This class is used to capture and validate incoming message requests.
 * It enforces that the message field is not blank using Jakarta Bean Validation.
 * </p>
 */
@Data
public class MessageRequest {

    /**
     * The message content to be published to Kafka.
     * <p>
     * Validation: This field must not be blank. An empty or null message will result in a validation error.
     * </p>
     */
    @NotBlank(message = "Message cannot be blank")
    private String message;
}
