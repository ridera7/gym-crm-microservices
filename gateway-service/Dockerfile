FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/gateway-service-0.0.1-SNAPSHOT.jar gateway-service.jar
ENV EUREKA_URI=http://localhost:8761/eureka
ENTRYPOINT ["java", "-jar", "gateway-service.jar"]