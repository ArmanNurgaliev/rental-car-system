FROM eclipse-temurin:21-jdk-alpine

COPY target/*.jar notification-service.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "/notification-service.jar"]