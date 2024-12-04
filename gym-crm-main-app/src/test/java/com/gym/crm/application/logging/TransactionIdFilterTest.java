package com.gym.crm.application.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@Slf4j
@ExtendWith(MockitoExtension.class)
class TransactionIdFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private TransactionIdFilter transactionIdFilter;

    @Test
    void shouldClearMDC_AfterProcessing() throws ServletException, IOException {
        transactionIdFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(MDC.get("transactionId"));
    }

    @Test
    void shouldClearMDC_EvenIfExceptionOccurs() throws ServletException, IOException {
        doThrow(new ServletException()).when(filterChain).doFilter(request, response);

        try {
            transactionIdFilter.doFilterInternal(request, response, filterChain);
        } catch (ServletException ignored) {
        }

        assertNull(MDC.get("transactionId"));
    }

}