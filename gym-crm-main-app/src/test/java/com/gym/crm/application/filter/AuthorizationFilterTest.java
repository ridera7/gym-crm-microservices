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
class AuthorizationFilterTest {

    @InjectMocks
    private AuthorizationFilter filter;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain chain;
    @Mock
    private PrintWriter printWriter;

    @Test
    void testDoFilter_MissingAuthorizationHeader() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn(null);
        when(response.getWriter()).thenReturn(printWriter);

        filter.doFilter(request, response, chain);

        verify(response).setStatus(401);
        verify(response.getWriter()).write("Missing Authorization Bearer token");
        verifyNoInteractions(chain);
    }

    @Test
    void testDoFilter_InvalidAuthorizationHeader() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn("InvalidToken");
        when(response.getWriter()).thenReturn(printWriter);

        filter.doFilter(request, response, chain);

        verify(response).setStatus(401);
        verify(response.getWriter()).write("Missing Authorization Bearer token");
        verifyNoInteractions(chain);
    }

    @Test
    void testDoFilter_ValidAuthorizationHeader() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        verify(response, never()).setStatus(401);
    }
}
