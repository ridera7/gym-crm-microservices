package com.gym.crm.application.service.impl;

import com.gym.crm.application.entity.User;
import com.gym.crm.application.exception.NotFoundException;
import com.gym.crm.application.exception.ValidationException;
import com.gym.crm.application.repository.UserRepository;
import com.gym.crm.application.service.UserService;
import com.gym.crm.application.service.impl.serviceutils.PasswordHandler;
import com.gym.crm.application.service.impl.validation.EntityValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.gym.crm.application.constant.Message.ENTITY_CANNOT_BE_NULL_ERROR_TEMPLATE;
import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final EntityValidator validator;
    private final PasswordHandler passwordHandler;

    @Override
    @Transactional
    public User save(User user) {
        Optional.ofNullable(user)
                .orElseThrow(() -> new ValidationException(format(ENTITY_CANNOT_BE_NULL_ERROR_TEMPLATE, "User")));

        User newUser = user.toBuilder()
                .username(createUsername(user.getFirstName() + "." + user.getLastName()))
                .password(passwordHandler.hashPassword(user.getPassword()))
                .isActive(true)
                .build();
        validator.checkUserValidation(newUser);

        repository.save(newUser);

        log.info("New user Id = {} saved", newUser.getId());

        return newUser;
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        validator.validateEntityId(id);

        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    @Transactional
    public void update(User user) {
        validator.validateEntityId(user.getId());
        validator.checkUserValidation(user);

        repository.save(user);
    }

    @Override
    @Transactional
    public void delete(User user) {
        validator.validateEntityId(user.getId());

        repository.deleteById(user.getId());
    }

    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public void changePassword(User user, String newPassword) {
        repository.save(user.toBuilder()
                .password(passwordHandler.hashPassword(newPassword))
                .build());
    }

    @Override
    @Transactional
    public void activateUser(String username, boolean isActive) {
        boolean activate = Optional.of(isActive)
                .orElseThrow(() -> new ValidationException("isActive is missing"));

        User user = Optional.ofNullable(repository.findByUsername(username))
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (user.isActive() != activate) {
            repository.save(user.toBuilder().isActive(activate).build());
        }
    }

    @Override
    public boolean isAuthenticate(String username, String password) {
        Optional<User> probeUser = Optional.ofNullable(repository.findByUsername(username));

        String storedPassword = probeUser.map(User::getPassword).orElse("");

        if (!passwordHandler.isPasswordMatch(password, storedPassword)) {
            log.error("Password not match");
            return false;
        }

        return true;

    }

    public String createUsername(String baseUsername) {
        List<String> usernames = repository.findByUsernameStartsWith(baseUsername).stream()
                .map(User::getUsername).toList();

        if (!usernames.contains(baseUsername)) {
            return baseUsername;
        }

        int suffix = calculateSuffix(usernames, baseUsername);

        return baseUsername + suffix;
    }

    private static int calculateSuffix(List<String> usernames, String baseUsername) {
        int suffix = 1;

        while (usernames.contains(baseUsername + suffix)) {
            suffix++;
        }

        return suffix;
    }

}
