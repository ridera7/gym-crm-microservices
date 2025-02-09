FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/service-working-hours-0.0.1-SNAPSHOT.jar service-working-hours.jar
ENV EUREKA_URI=http://localhost:8761/eureka
ENTRYPOINT ["java", "-jar", "service-working-hours.jar"]