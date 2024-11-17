FROM maven:3.8.5-openjdk AS build

COPY . .
RUN mvn clean package -DskipTests
FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/IOBANK-0.0.1-SNAPSHOT.jar iobank.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "iobank.jar"]