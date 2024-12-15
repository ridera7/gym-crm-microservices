package com.authentication.service.security;

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

import static org.junit.jupiter.api.Assertions.assertThrows;
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
    public void shouldAuthenticate_Success() {
        String username = "user1";
        String password = "password";
        UserDetails userDetails = mock(UserDetails.class);
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        when(bruteforceProtector.isBlocked(username)).thenReturn(false);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(passwordEncoder.matches(password, userDetails.getPassword())).thenReturn(true);

        Authentication actual = customAuthenticationProvider.authenticate(authentication);

        assert(actual instanceof UsernamePasswordAuthenticationToken);
        verify(bruteforceProtector).loginSucceeded(username);
    }

    @Test
    public void shouldThrowExceptionIfUserIsBlocked() {
        String username = "user2";
        String password = "password";
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        when(bruteforceProtector.isBlocked(username)).thenReturn(true);

        assertThrows(BadCredentialsException.class, () -> customAuthenticationProvider.authenticate(authentication));
    }

    @Test
    public void shouldThrowExceptionIfInvalidUsername() {
        String username = "user3";
        String password = "password";
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        when(bruteforceProtector.isBlocked(username)).thenReturn(false);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(null);

        assertThrows(BadCredentialsException.class, () -> customAuthenticationProvider.authenticate(authentication));

        verify(bruteforceProtector).loginFailed(username);
    }

    @Test
    public void shouldThrowExceptionIfInvalidPassword() {
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
    public void shouldSupports() {
        assert(customAuthenticationProvider.supports(UsernamePasswordAuthenticationToken.class));
        assert(!customAuthenticationProvider.supports(Object.class));
    }

}