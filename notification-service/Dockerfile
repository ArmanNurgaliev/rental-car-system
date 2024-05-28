FROM eclipse-temurin:21-jdk-alpine

COPY target/*.jar notification-service.jar

ENTRYPOINT ["java", "-jar", "/notification-service.jar"]