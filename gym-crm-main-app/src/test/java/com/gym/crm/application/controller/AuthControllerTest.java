package com.gym.crm.application.controller;

import com.gym.crm.application.rest.dto.LoginChangeRequest;
import com.gym.crm.application.rest.dto.LoginCredentials;
import com.gym.crm.application.security.UserAuthenticator;
import com.gym.crm.application.service.facade.ServiceFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private ServiceFacade serviceFacade;

    @Mock
    private UserAuthenticator userAuthenticator;

    @InjectMocks
    private AuthController authController;

    @Test
    void shouldLogin() {
        LoginCredentials loginCredentials = new LoginCredentials("username", "password");

        authController.login(loginCredentials);

        verify(userAuthenticator).authenticate(any());
    }

    @Test
    void shouldLoginChange() {
        LoginChangeRequest loginChangeRequest = new LoginChangeRequest()
                .username("Ivan.Ivanoff")
                .oldPassword("oldPassword")
                .newPassword("newPassword");

        authController.loginChange(loginChangeRequest);

        verify(serviceFacade).changeUserLogin(loginChangeRequest);
    }

}