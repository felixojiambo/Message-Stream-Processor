# Message Stream Processor

Message Stream Processor is a Spring Boot-based microservice that enables message publishing and consumption using Apache Kafka. This project leverages Docker for containerization and Kubernetes for orchestration, providing a scalable solution for real-time messaging.

## Overview

The Message Stream Processor application is designed to:

- **Publish Messages:** Accept messages via a REST API endpoint and send them to a Kafka topic.
- **Consume Messages:** Listen to a Kafka topic and process incoming messages.
- **Handle Errors:** Use a robust error handling mechanism with retry logic and a dead-letter topic.
- **Deploy Easily:** Use Docker multi-stage builds for optimized images and Kubernetes manifests for seamless deployment.

## Features

- **Spring Boot REST API:** Provides a `/publish` endpoint for message submission.
- **Kafka Integration:** 
  - **Producer:** Publishes messages to a designated Kafka topic.
  - **Consumer:** Listens on the Kafka topic and logs or processes messages.
  - **Error Handling:** Implements an exponential backoff strategy with retries and routes failed messages to a dead-letter topic.
- **Containerization:** Multi-stage Docker builds utilizing IBM Semeru JDK/JRE for build and runtime optimization.
- **Kubernetes Support:** YAML manifests for deploying Kafka, Zookeeper, and the Spring Boot application.

## Architecture

The project is composed of the following layers:

1. **API Layer:**
   - **MessageController:** Handles incoming HTTP POST requests to publish messages.
2. **Service Layer:**
   - **KafkaProducerService:** Manages the publishing of messages to Kafka.
   - **KafkaConsumerService:** Consumes messages from Kafka and processes them.
3. **Error Handling:**
   - Configured via custom error handlers that implement retries and dead-letter routing.
4. **Infrastructure:**
   - **Kafka and Zookeeper:** Managed with Docker and Kubernetes.
   - **Kubernetes Manifests:** Provide configuration for deployments, services, and externalized configuration via ConfigMaps.

## Technologies Used

- **Java 21 (IBM Semeru JDK/JRE)**
- **Spring Boot**
- **Apache Kafka**
- **Docker**
- **Kubernetes**
- **Maven**
- **Lombok**

## Getting Started

### Prerequisites

- **Java 21**
- **Maven 3.6+**
- **Docker**
- **Kubernetes (e.g., minikube, kind, or any managed cluster)**
- **Kafka and Zookeeper** (can be run via Docker Compose or Kubernetes)

### Building the Application

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/felixojiambo/Message-Stream-Processor.git
   cd Message-Stream-Processor
   ```

2. **Build with Maven:**

   ```bash
   ./mvnw clean package -DskipTests
   ```

3. **Build the Docker Image:**

   ```bash
   docker build -t message-stream-processor:latest .
   ```

### Running Locally with Docker Compose

Use the provided Docker Compose file to launch Zookeeper, Kafka, and the Message Stream Processor:

```bash
docker-compose up
```

### Deploying on Kubernetes

1. **Apply the ConfigMap:**

   ```bash
   kubectl apply -f kubernetes/kafka-configmap.yaml
   ```

2. **Deploy Zookeeper:**

   ```bash
   kubectl apply -f kubernetes/zookeeper-deployment.yaml
   kubectl apply -f kubernetes/zookeeper-service.yaml
   ```

3. **Deploy Kafka:**

   ```bash
   kubectl apply -f kubernetes/kafka-deployment.yaml
   kubectl apply -f kubernetes/kafka-service.yaml
   ```

4. **Deploy the Spring Boot Application:**

   ```bash
   kubectl apply -f kubernetes/spring-app-deployment.yaml
   kubectl apply -f kubernetes/spring-app-service.yaml
   ```

## Usage

- **Publishing a Message:**

  Send a POST request to the `/publish` endpoint with a JSON payload:

  ```json
  {
    "message": "Hello, Kafka!"
  }
  ```

  Example using `curl`:

  ```bash
  curl -X POST http://localhost:80/publish \
       -H "Content-Type: application/json" \
       -d '{"message": "Hello, Kafka!"}'
  ```

- **Consuming Messages:**

  The Kafka consumer listens on the configured topic and logs incoming messages. Check the logs to see the processed messages and error handling in action.

## Contributing

Contributions are welcome! To get started, fork the repository and submit pull requests with your enhancements or bug fixes. For issues or feature requests, please open an issue on GitHub.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
