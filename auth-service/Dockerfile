FROM eclipse-temurin:21-jdk-alpine

COPY target/*.jar auth-service.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "/auth-service.jar"]