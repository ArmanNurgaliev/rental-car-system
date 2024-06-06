FROM eclipse-temurin:21-jdk-alpine

COPY target/*.jar reservation-service.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "/reservation-service.jar"]