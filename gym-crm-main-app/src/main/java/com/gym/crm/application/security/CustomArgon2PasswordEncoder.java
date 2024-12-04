package com.gym.crm.application.security;

import com.gym.crm.application.service.impl.serviceutils.PasswordHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomArgon2PasswordEncoder implements PasswordEncoder {

    private final PasswordHandler delegate;

    @Override
    public String encode(CharSequence rawPassword) {
        return delegate.hashPassword(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String storedPassword) {
        return delegate.isPasswordMatch(rawPassword.toString(), storedPassword);
    }

}
