# Stage 1: Build the application using Maven
FROM maven:3.8.4-openjdk-17-slim AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and source code to the container
COPY pom.xml /app
COPY src /app/src

# Build the Spring Boot application
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM openjdk:17-jdk-slim

# Set the working directory for the runtime image
WORKDIR /app

# Copy the jar file built in the previous stage
COPY --from=build /app/target/DigitalTimeCapsule-0.0.1-SNAPSHOT.jar /app/DigitalTimeCapsule.jar

# Expose port 8080 (or whatever port your Spring Boot app is using)
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/DigitalTimeCapsule.jar"]