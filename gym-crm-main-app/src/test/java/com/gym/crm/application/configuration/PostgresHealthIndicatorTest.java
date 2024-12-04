package com.gym.crm.application.configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostgresHealthIndicatorTest {

    public static final Status DOWN_STATUS = Health.down().build().getStatus();

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private Statement statement;

    @InjectMocks
    private PostgresHealthIndicator sut;

    @Test
    void shouldSetHealthStatusIsUpWhenDatabaseIsReachable() throws SQLException {
        String expectedMessageStatus = "Database is reachable";
        String testSqlQuery = "SELECT 1";
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.execute(testSqlQuery)).thenReturn(true);

        Health actual = sut.health();

        assertEquals(Health.up().withDetail("PostgreSQL", expectedMessageStatus).build().getStatus(), actual.getStatus());
        assertEquals(expectedMessageStatus, actual.getDetails().get("PostgreSQL"));

        verify(connection).close();
        verify(statement).close();
    }

    @Test
    void shouldSetHealthStatusIsDownWhenDatabaseIsNotReachable() throws SQLException {
        String expectedNotReachable = "Database is not reachable";
        String expectedErrorMessage = "java.sql.SQLException: Database unreachable";
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenThrow(new SQLException("Database unreachable"));

        Health actual = sut.health();

        assertEquals(DOWN_STATUS, actual.getStatus());
        assertEquals(expectedNotReachable, actual.getDetails().get("PostgreSQL"));
        assertEquals(expectedErrorMessage, actual.getDetails().get("error"));

        verify(connection).close();
    }

    @Test
    void shouldSetHealthStatusIsDownWhenConnectionIsNull() throws SQLException {
        when(dataSource.getConnection()).thenReturn(null);

        Health actual = sut.health();

        assertEquals(DOWN_STATUS, actual.getStatus());
    }

}