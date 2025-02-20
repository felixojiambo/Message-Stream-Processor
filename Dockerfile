# Use IBM Semeru JDK 21 as the builder image for compilation.
# This image includes JDK 21, suitable for compiling Java applications.
FROM ibm-semeru-runtimes:open-21-jdk AS builder

# Set the working directory for the build process inside the container.
WORKDIR /message-stream-processor

# Copy only the Maven wrapper files and the POM file first.
# This allows Docker to cache dependencies efficiently, speeding up future builds.
COPY .mvn .mvn
COPY mvnw pom.xml ./

# Ensure the Maven wrapper and scripts have execution permissions.
RUN chmod +x ./mvnw && chmod -R +x .mvn

# Verify that Maven wrapper is working and JDK is configured correctly.
RUN ./mvnw --version

# Copy the remaining source code into the container after caching dependencies.
COPY . .

# Build the application using Maven, skipping tests for faster build times.
RUN ./mvnw clean package -DskipTests

# Rename the resulting JAR file for uniformity and convenience.
RUN mv target/*.jar target/app.jar

# Extract the Spring Boot layered JAR for optimized Docker image layering.
# This step is useful if the application is packaged as a layered JAR.
RUN java -Djarmode=tools -jar target/app.jar extract || true

# Use IBM Semeru JRE 21 as the runtime image.
# This image is smaller than the JDK, reducing the size of the final image.
FROM ibm-semeru-runtimes:open-21-jre

# Set the working directory for the runtime application.
WORKDIR /message-stream-processor

# Copy the built JAR file from the builder stage into the runtime image.
COPY --from=builder /message-stream-processor/target/app.jar /message-stream-processor/app.jar

# Create a directory for Class Data Sharing (CDS) cache, improving JVM startup time.
RUN mkdir -p /message-stream-processor/class-cache && \
    java -Xshareclasses:name=springCache,cacheDir=/message-stream-processor/class-cache \
    -jar -Dspring.profiles.active=cds -Dspring.context.exit-onRefresh /message-stream-processor/app.jar || true

# Expose debugging port (5005) and run the Spring Boot application with remote debugging enabled.
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "/message-stream-processor/app.jar"]
