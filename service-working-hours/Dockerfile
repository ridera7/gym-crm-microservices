FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/service-working-hours-0.0.1-SNAPSHOT.jar service-working-hours.jar
ENV EUREKA_URI=http://localhost:8761/eureka
ENV MONGO_URL=mongodb://127.0.0.1:27017/working_hours
ENV MONGO_USER=gcauser
ENV MONGO_PASSWORD=gcapass
ENV BROKER_URL=tcp://localhost:61616
ENV BROKER_USER=gcauser
ENV BROKER_PASSWORD=gcapass
ENTRYPOINT ["java", "-jar", "service-working-hours.jar"]