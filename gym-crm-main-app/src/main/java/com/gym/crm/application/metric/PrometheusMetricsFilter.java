package com.gym.crm.application.metric;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Order(1)
@Component
public class PrometheusMetricsFilter implements Filter {

    private final Counter requestCounter;
    private final Counter errorCounter;

    public PrometheusMetricsFilter(MeterRegistry meterRegistry) {
        this.requestCounter = meterRegistry.counter("http_requests_total", "type", "all");

        this.errorCounter = meterRegistry.counter("http_requests_errors_total", "type", "error");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        requestCounter.increment();

        try {
            chain.doFilter(request, response);

            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            int status = httpServletResponse.getStatus();

            if (status >= 400) {
                errorCounter.increment();
            }
        } catch (Exception e) {
            errorCounter.increment();
            throw e;
        }
    }

}
