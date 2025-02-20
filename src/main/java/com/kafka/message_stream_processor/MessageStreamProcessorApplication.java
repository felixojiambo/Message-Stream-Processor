package com.kafka.message_stream_processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MessageStreamProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessageStreamProcessorApplication.class, args);
		System.out.println("Hello World!");
	}

}
