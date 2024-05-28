FROM eclipse-temurin:21-jdk-alpine

COPY target/*.jar api-gateway.jar

ENTRYPOINT ["java", "-jar", "/api-gateway.jar"]