FROM eclipse-temurin:21-jdk-alpine

COPY target/*.jar discovery-server.jar

ENTRYPOINT ["java", "-jar", "/discovery-server.jar"]