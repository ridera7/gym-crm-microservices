FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/gym-crm-main-app-0.0.1-SNAPSHOT.jar gym-crm-main-app.jar
ENV DB_URL="jdbc:postgresql://gymcrm-postgres.c74quikwgmxz.us-east-1.rds.amazonaws.com:5432/gca_db"
ENV DB_USER=gcauser
ENV DB_PASSWORD=gcapass
ENV BROKER_URL=tcp://localhost:61616
ENV BROKER_USER=gcauser
ENV BROKER_PASSWORD=gcapass
ENV EUREKA_URI=http://localhost:8761/eureka
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "gym-crm-main-app.jar"]