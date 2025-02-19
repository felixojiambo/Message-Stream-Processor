# Use IBM Semeru JDK 21 as the builder image for compilation
FROM ibm-semeru-runtimes:open-21-jdk AS builder

# Set the working directory for the build process
WORKDIR /message-stream-processor

# Copy only the Maven wrapper files first to optimize Docker caching
COPY .mvn .mvn
COPY mvnw pom.xml ./

# Ensure the Maven wrapper has execution permissions
RUN chmod +x ./mvnw && chmod -R +x .mvn

# Verify that Maven is available
RUN ./mvnw --version

# Copy the entire source code after leveraging cache efficiency
COPY . .

# Build the project, skipping tests for faster builds
RUN ./mvnw clean package -DskipTests

# Rename the built JAR for consistency and easier referencing
RUN mv target/*.jar target/app.jar

# Extract layers for optimized startup if using Spring Boot layered JARs
RUN java -Djarmode=tools -jar target/app.jar extract || true

# Use IBM Semeru JRE 21 for the runtime image (smaller than JDK)
FROM ibm-semeru-runtimes:open-21-jre

# Set the working directory in the runtime container
WORKDIR /message-stream-processor

# Copy the built application from the builder stage
COPY --from=builder /message-stream-processor/target/app.jar /message-stream-processor/app.jar

# Pre-build the CDS (Class Data Sharing) cache for improved startup performance
RUN mkdir -p /message-stream-processor/class-cache && \
    java -Xshareclasses:name=springCache,cacheDir=/message-stream-processor/class-cache \
    -jar -Dspring.profiles.active=cds -Dspring.context.exit-onRefresh /message-stream-processor/app.jar || true

# Define the default command to run the Spring Boot application
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "/message-stream-processor/app.jar"]
