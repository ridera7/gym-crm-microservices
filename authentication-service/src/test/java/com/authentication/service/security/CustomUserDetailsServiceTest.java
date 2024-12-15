package com.authentication.service.security;

import com.authentication.service.entity.UserProjection;
import com.authentication.service.exception.NotFoundException;
import com.authentication.service.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private UserRepository userRepository;

    @Test
    void shouldLoadUserByUsername_UserExists() {
        String username = "testUser";
        String password = "testPassword";
        UserProjection userProjection = mock(UserProjection.class);

        when(userProjection.getUsername()).thenReturn(username);
        when(userProjection.getPassword()).thenReturn(password);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userProjection));

        UserDetails actualUserDetails = customUserDetailsService.loadUserByUsername(username);

        assertEquals(username, actualUserDetails.getUsername());
        assertEquals(password, actualUserDetails.getPassword());
        assertTrue(actualUserDetails.getAuthorities().isEmpty());

        verify(userRepository).findByUsername(username);
    }

    @Test
    void shouldThrowExceptionIfUserNotFound() {
        String username = "nonExistentUser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(username));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findByUsername(username);
    }
}
