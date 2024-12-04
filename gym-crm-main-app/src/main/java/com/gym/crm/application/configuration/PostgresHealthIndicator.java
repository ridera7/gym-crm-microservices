package com.gym.crm.application.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Component
@RequiredArgsConstructor
public class PostgresHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;

    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("SELECT 1");

            return Health.up().withDetail("PostgreSQL", "Database is reachable").build();
        } catch (Exception e) {
            return Health.down().withDetail("PostgreSQL", "Database is not reachable").withException(e).build();
        }
    }

}
