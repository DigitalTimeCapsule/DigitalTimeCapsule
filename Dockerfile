FROM maven:3.8.2-openjdk-17 AS build

WORKDIR /app/backend/digitaltimecapsule

COPY backend/digitaltimecapsule /app/backend/digitaltimecapsule

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

COPY --from=build /app/backend/digitaltimecapsule/target/digitaltimecapsule-0.0.1-SNAPSHOT.jar DigitalTimeCapsule.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "DigitalTimeCapsule.jar"]
