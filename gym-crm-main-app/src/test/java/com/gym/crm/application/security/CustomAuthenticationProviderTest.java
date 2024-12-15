package com.gym.crm.application.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationProviderTest {

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private CustomArgon2PasswordEncoder passwordEncoder;

    @Mock
    private BruteforceProtector bruteforceProtector;

    @InjectMocks
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Test
    public void testAuthenticate_Success() {
        String username = "user1";
        String password = "password";
        UserDetails userDetails = mock(UserDetails.class);
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        when(bruteforceProtector.isBlocked(username)).thenReturn(false);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(passwordEncoder.matches(password, userDetails.getPassword())).thenReturn(true);

        Authentication result = customAuthenticationProvider.authenticate(authentication);

        assertInstanceOf(UsernamePasswordAuthenticationToken.class, result);
        verify(bruteforceProtector).loginSucceeded(username);
    }

    @Test
    public void testAuthenticate_UserIsBlocked() {
        String username = "user2";
        String password = "password";
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        when(bruteforceProtector.isBlocked(username)).thenReturn(true);

        assertThrows(BadCredentialsException.class, () -> customAuthenticationProvider.authenticate(authentication));
    }

    @Test
    public void testAuthenticate_InvalidUsername() {
        String username = "user3";
        String password = "password";
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        when(bruteforceProtector.isBlocked(username)).thenReturn(false);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(null);

        assertThrows(BadCredentialsException.class, () -> customAuthenticationProvider.authenticate(authentication));

        verify(bruteforceProtector).loginFailed(username);
    }

    @Test
    public void testAuthenticate_InvalidPassword() {
        String username = "user4";
        String password = "wrongPassword";
        UserDetails userDetails = mock(UserDetails.class);
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        when(bruteforceProtector.isBlocked(username)).thenReturn(false);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(passwordEncoder.matches(password, userDetails.getPassword())).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> customAuthenticationProvider.authenticate(authentication));

        verify(bruteforceProtector).loginFailed(username);
    }

    @Test
    public void testSupports() {
        assertTrue(customAuthenticationProvider.supports(UsernamePasswordAuthenticationToken.class));
        assertFalse(customAuthenticationProvider.supports(Object.class));
    }

}