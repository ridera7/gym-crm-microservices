package com.gym.crm.application.metric;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrometheusMetricsFilterTest {

    @Mock
    private Counter requestCounter;

    @Mock
    private Counter errorCounter;

    @Mock
    private MeterRegistry meterRegistry;

    @Mock
    private FilterChain filterChain;

    @Mock
    private ServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private PrometheusMetricsFilter filter;

    @BeforeEach
    void setUp() {
        when(meterRegistry.counter("http_requests_total", "type", "all")).thenReturn(requestCounter);
        when(meterRegistry.counter("http_requests_errors_total", "type", "error")).thenReturn(errorCounter);
        filter = new PrometheusMetricsFilter(meterRegistry);
    }

    @Test
    void shouldIncrementsRequestCounterOnEveryRequest() throws IOException, ServletException {
        when(response.getStatus()).thenReturn(200);

        filter.doFilter(request, response, filterChain);

        verify(requestCounter).increment();
        verify(errorCounter, never()).increment();
    }

    @Test
    void shouldIncrementsErrorCounterOnErrorStatus() throws IOException, ServletException {
        when(response.getStatus()).thenReturn(500);

        filter.doFilter(request, response, filterChain);

        verify(requestCounter).increment();
        verify(errorCounter).increment();
    }

    @Test
    void shouldIncrementsErrorCounterOnException() throws IOException, ServletException {
        doThrow(new ServletException("Test exception")).when(filterChain).doFilter(request, response);

        assertThrows(ServletException.class, () -> filter.doFilter(request, response, filterChain));

        verify(requestCounter).increment();
        verify(errorCounter).increment();
    }

}