# Build stage
FROM maven:3.8.5-openjdk-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/IOBANK-0.0.1-SNAPSHOT.jar iobank.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "iobank.jar"]