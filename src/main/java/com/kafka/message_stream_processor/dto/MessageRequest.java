package com.kafka.message_stream_processor.dto;
import lombok.Data;

/**
 * Data Transfer Object for message payloads.
 */
@Data
public class MessageRequest {
    private String message;
}
