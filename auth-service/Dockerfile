FROM eclipse-temurin:21-jdk-alpine

COPY target/*.jar auth-service.jar

ENTRYPOINT ["java", "-jar", "/auth-service.jar"]