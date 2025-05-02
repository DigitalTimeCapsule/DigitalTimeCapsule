# Build stage
FROM maven:3.8.2-jdk-11 AS build

# Copy the entire project into the container
COPY . .

# Build the Spring Boot app with Maven (skip tests for faster build)
RUN mvn clean package -DskipTests

# Package stage
FROM openjdk:11-jdk-slim

# Copy the built .jar file from the build stage
COPY --from=build /target/DigitalTimeCapsule-0.0.1-SNAPSHOT.jar DigitalTimeCapsule.jar

# Expose the port that the app will run on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "DigitalTimeCapsule.jar"]
