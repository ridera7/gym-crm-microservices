package com.authentication.service.logging;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Order(2)
@Component
public class LoggingFilter implements Filter {

    private static final String[] SENSITIVE_FIELDS = {"password", "oldPassword", "newPassword"};

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        ContentCachingRequestWrapper wrappedRequest =
                new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper wrappedResponse =
                new ContentCachingResponseWrapper((HttpServletResponse) response);

        try {
            chain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            String transactionId = MDC.get("transactionId");

            logRequest(wrappedRequest, transactionId);
            logResponse(wrappedResponse, transactionId);

            wrappedResponse.copyBodyToResponse();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request, String transactionId) {
        String requestBody = getContentAsString(request.getContentAsByteArray());
        String obfuscatedRequestBody = obfuscateSensitiveInformation(requestBody);

        log.info("Incoming Request: [transactionId={}, method={}, URI={}, body={}]",
                transactionId,
                request.getMethod(),
                request.getRequestURI(),
                obfuscatedRequestBody);
    }

    private void logResponse(ContentCachingResponseWrapper response, String transactionId) {
        String responseBody = getContentAsString(response.getContentAsByteArray());
        String obfuscatedResponseBody = obfuscateSensitiveInformation(responseBody);
        int status = response.getStatus();

        if (status >= 400) {
            log.error("Error Response: [transactionId={}, status={}, body={}]", transactionId, status, obfuscatedResponseBody);
        } else {
            log.info("Response: [transactionId={}, status={}, body={}]", transactionId, status, obfuscatedResponseBody);
        }
    }

    private String getContentAsString(byte[] content) {
        try {
            return new String(content, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "[Error reading content]";
        }
    }

    private String obfuscateSensitiveInformation(String content) {
        if (content == null || content.isEmpty()) {

            return content;
        }

        for (String field : SENSITIVE_FIELDS) {
            content = content.replaceAll("(\"" + field + "\":\\s?\")([^\"]*)\"", "$1***\"");
        }

        return content;
    }

}
