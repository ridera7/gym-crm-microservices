FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/eureka-service-0.0.1-SNAPSHOT.jar eureka-service.jar
ENTRYPOINT ["java", "-jar", "eureka-service.jar"]