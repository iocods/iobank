# Build stage
FROM maven:3.9.5-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/IOBANK-0.0.1-SNAPSHOT.jar iobank.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "iobank.jar"]