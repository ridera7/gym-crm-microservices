package com.service.working.hours.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GatewayFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String xRequestX = httpRequest.getHeader("X-Request-X");

        if (xRequestX == null || xRequestX.isEmpty()) {
            httpResponse.setStatus(400);
            httpResponse.getWriter().write("Missing X-Request-X header");
            return;
        }

        if (!"Gateway".equals(xRequestX)) {
            httpResponse.setStatus(400);
            httpResponse.getWriter().write("Invalid X-Request-X header value");
            return;
        }

        chain.doFilter(request, response);
    }

}
