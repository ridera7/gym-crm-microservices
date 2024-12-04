package com.gym.crm.application.security;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@Tag("skipgithub")
@SpringBootTest
@ActiveProfiles("test")
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private HttpSecurity http;

    @InjectMocks
    private SecurityConfig securityConfig;

    @Test
    void shouldCreateRightSecurityFilterChain() throws Exception {
        when(http.cors(any())).thenReturn(http);
        when(http.csrf(any())).thenReturn(http);
        when(http.authorizeHttpRequests(any())).thenReturn(http);
        when(http.addFilterBefore(any(), any())).thenReturn(http);
        when(http.logout(any())).thenReturn(http);

        securityConfig.securityFilterChain(http);

        verify(http).cors(any());
        verify(http).csrf(any());
        verify(http).authorizeHttpRequests(any());
        verify(http).addFilterBefore(any(), any());
        verify(http).logout(any());
    }

    @Test
    void shouldCreatePasswordEncoderBean() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        assertNotNull(encoder, "PasswordEncoder should not be null");
    }

    @ParameterizedTest
    @MethodSource("excludedUrlProviderGet")
    void shouldAllowAccessToExcludedUrlsGetWithoutAuthentication(String url) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("excludedUrlProviderPost")
    void shouldAllowAccessToExcludedUrlsPostWithoutAuthentication(String url) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(url))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldAuthenticateAndAuthorizeUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/login")
                        .with(user("user").password("password").roles("USER"))
                        .with(csrf()))
                .andExpect(authenticated());
    }

    private static Stream<Arguments> excludedUrlProviderGet() {
        return Stream.of("/api-docs",
                        "/actuator")
                .map(Arguments::of);
    }

    private static Stream<Arguments> excludedUrlProviderPost() {
        return Stream.of("/api/v1/login",
                        "/api/v1/trainee/register",
                        "/api/v1/trainer/register",
                        "/api/v1/swagger-ui")
                .map(Arguments::of);
    }

}