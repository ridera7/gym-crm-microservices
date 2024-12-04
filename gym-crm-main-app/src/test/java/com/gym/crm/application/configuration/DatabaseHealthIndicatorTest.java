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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DatabaseHealthIndicatorTest {

    public static final Status DOWN_STATUS = Health.down().build().getStatus();

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @InjectMocks
    private DatabaseHealthIndicator sut;

    @Test
    void shouldSetHealthStatusIsUpWhenConnectionIsValid() throws SQLException {
        String expected = "Available";
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.isValid(1)).thenReturn(true);

        Health actual = sut.health();

        assertEquals(Health.up().withDetail("Database", "Available").build().getStatus(), actual.getStatus());
        assertEquals(expected, actual.getDetails().get("Database"));
        verify(connection).close();
    }

    @Test
    void shouldSetHealthStatusIsDownWhenConnectionIsNotValid() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.isValid(1)).thenReturn(false);

        Health actual = sut.health();

        assertEquals(DOWN_STATUS, actual.getStatus());
        verify(connection).close();
    }

    @Test
    void shouldSetHealthStatusIsDownWithExceptionWhenConnectionThrowsException() throws SQLException {
        String expectedNotAvailable = "Not available";
        String expectedErrorMessage = "java.sql.SQLException: Connection failed";
        when(dataSource.getConnection()).thenThrow(new SQLException("Connection failed"));

        Health actual = sut.health();

        assertEquals(DOWN_STATUS, actual.getStatus());
        assertEquals(expectedNotAvailable, actual.getDetails().get("Database"));
        assertEquals(expectedErrorMessage, actual.getDetails().get("error"));
    }

    @Test
    void shouldSetHealthStatusIsDownWhenConnectionIsNull() throws SQLException {
        when(dataSource.getConnection()).thenReturn(null);

        Health actual = sut.health();

        assertEquals(DOWN_STATUS, actual.getStatus());
    }

}