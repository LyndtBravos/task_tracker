FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /build/target/*.jar task_tracker.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "task_tracker.jar"]