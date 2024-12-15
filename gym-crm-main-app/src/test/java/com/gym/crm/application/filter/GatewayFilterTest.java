package com.gym.crm.application.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GatewayFilterTest {

    @InjectMocks
    private GatewayFilter filter;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain chain;
    @Mock
    private PrintWriter printWriter;

    @Test
    void testDoFilter_MissingXRequestXHeader() throws IOException, ServletException {
        when(request.getHeader("X-Request-X")).thenReturn(null);
        when(response.getWriter()).thenReturn(printWriter);

        filter.doFilter(request, response, chain);

        verify(response).setStatus(400);
        verify(response.getWriter()).write("Missing X-Request-X header");
        verifyNoInteractions(chain);
    }

    @Test
    void testDoFilter_EmptyXRequestXHeader() throws IOException, ServletException {
        when(request.getHeader("X-Request-X")).thenReturn("");
        when(response.getWriter()).thenReturn(printWriter);

        filter.doFilter(request, response, chain);

        verify(response).setStatus(400);
        verify(response.getWriter()).write("Missing X-Request-X header");
        verifyNoInteractions(chain);
    }

    @Test
    void testDoFilter_InvalidXRequestXHeaderValue() throws IOException, ServletException {
        when(request.getHeader("X-Request-X")).thenReturn("InvalidValue");
        when(response.getWriter()).thenReturn(printWriter);

        filter.doFilter(request, response, chain);

        verify(response).setStatus(400);
        verify(response.getWriter()).write("Invalid X-Request-X header value");
        verifyNoInteractions(chain);
    }

    @Test
    void testDoFilter_ValidXRequestXHeaderValue() throws IOException, ServletException {
        when(request.getHeader("X-Request-X")).thenReturn("Gateway");

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        verify(response, never()).setStatus(400);
    }
}