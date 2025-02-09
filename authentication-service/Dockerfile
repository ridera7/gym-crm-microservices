FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/authentication-service-0.0.1-SNAPSHOT.jar authentication-service.jar
ENV DB_URL=jdbc:postgresql://localhost:5432/gca_db
ENV DB_USER=gcauser
ENV DB_PASSWORD=gcapass
ENV EUREKA_URI=http://localhost:8761/eureka
ENTRYPOINT ["java", "-jar", "authentication-service.jar"]