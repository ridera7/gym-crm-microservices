FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/gym-crm-main-app-0.0.1-SNAPSHOT.jar gym-crm-main-app.jar
ENV DB_URL="jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL"
ENV DB_USER=sa
ENV DB_PASSWORD=sa
ENV BROKER_URL=tcp://localhost:61616
ENV BROKER_USER=gcauser
ENV BROKER_PASSWORD=gcapass
ENV EUREKA_URI=http://localhost:8761/eureka
ENTRYPOINT ["java", "-jar", "gym-crm-main-app.jar"]