# Build stage
FROM maven:3.8.2-openjdk-17 AS build

# Set the working directory to the location of your pom.xml
WORKDIR /app/backend/digitaltimecapsule

# Copy the pom.xml and source code into the container
COPY backend/digitaltimecapsule /app/backend/digitaltimecapsule

# Build the Spring Boot app with Maven (skip tests for faster build)
RUN mvn clean package -DskipTests

# Package stage
FROM openjdk:11-jdk-slim

# Copy the built .jar file from the build stage
COPY --from=build /app/backend/digitaltimecapsule/target/digitaltimecapsule-0.0.1-SNAPSHOT.jar DigitalTimeCapsule.jar

# Expose the port that the app will run on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "DigitalTimeCapsule.jar"]
