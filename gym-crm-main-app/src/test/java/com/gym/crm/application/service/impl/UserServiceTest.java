package com.gym.crm.application.service.impl;

import com.gym.crm.application.entity.User;
import com.gym.crm.application.exception.ValidationException;
import com.gym.crm.application.repository.UserRepository;
import com.gym.crm.application.service.impl.serviceutils.PasswordHandler;
import com.gym.crm.application.service.impl.validation.EntityValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.gym.crm.application.testdata.EntityTestData.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityValidator entityValidator;

    @Mock
    private PasswordHandler passwordHandler;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Should create new user normally")
    public void shouldSaveNewUserNormally() {
        User userToStore = User.builder()
                .firstName("Ivan")
                .lastName("Ivanoff")
                .password("1234567890")
                .build();
        when(userRepository.findByUsernameStartsWith(anyString())).thenReturn(new ArrayList<>());
        when(passwordHandler.hashPassword(anyString()))
                .thenReturn("ebea09562570d10cccb58137cc211c18:eb08d4de685ff2bfe7f68be49ef8993f2af94c62a039dabdef5fafb607563499");

        User createdUser = userService.save(userToStore);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getUsername()).isEqualTo("Ivan.Ivanoff");
        assertThat(createdUser.getPassword()).hasSize(97);
        assertThat(createdUser.isActive()).isTrue();
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should create user with duplicate username")
    void shouldSaveUsernameWithDuplicate() {
        User userToStore = User.builder()
                .firstName("Ivan")
                .lastName("Ivanoff")
                .build();
        User existedUser = User.builder()
                .username("Ivan.Ivanoff").build();
        User anotherExistedUser = User.builder()
                .username("Ivan.Ivanoff1").build();
        List<User> existedUsernames = List.of(existedUser, anotherExistedUser);
        when(userRepository.findByUsernameStartsWith("Ivan.Ivanoff")).thenReturn(existedUsernames);

        User createdUser = userService.save(userToStore);

        assertThat(createdUser.getUsername()).isEqualTo("Ivan.Ivanoff2");
    }

    @Test
    @DisplayName("Should throw ValidationException when user is null")
    void shouldThrowValidationException_WhenUserIsNull() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.save(null));

        assertEquals("User can not be null", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should read by id proper user")
    void shouldReadByIdProperUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(USER));

        User foundUser = userService.findById(1L);

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(1);
        assertThat(foundUser.getFirstName()).isEqualTo("Ivan");
        assertThat(foundUser.getLastName()).isEqualTo("Ivanoff");
        verify(userRepository).findById(1L);
    }

    @Test
    void shouldReturnUserByUsernameIfUserExists() {
        String username = USER.getUsername();
        User mockUser = USER;
        when(userRepository.findByUsername(username)).thenReturn(mockUser);

        User result = userService.findByUsername(username);

        assertEquals(mockUser, result);
        verify(userRepository).findByUsername(username);
    }

    @Test
    void shouldReturnNullIfUserDoesNotExist() {
        String username = "nonexistentUser";
        when(userRepository.findByUsername(username)).thenReturn(null);

        User result = userService.findByUsername(username);

        assertNull(result);
        verify(userRepository).findByUsername(username);
    }
    
    @Test
    @DisplayName("Should call userRepository.save with the right argument")
    void shouldCallUpdateWithRightArgument() {
        userService.update(USER);

        verify(userRepository).save(USER);
    }

    @Test
    @DisplayName("Should call userRepository.deleteById with the right argument")
    void shouldCallDeleteWithRightArgument() {
        userService.delete(USER);

        verify(userRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should call userRepository.findAll and return full list")
    void shouldCallReadAllAndReturnFullList() {
        List<User> users = List.of(
                new User(1L, "Ivan", "Ivanoff", "Ivan.Ivanoff", "password123", true),
                new User(2L, "Petr", "Petroff", "Petr.Petroff", "password456", true)
        );
        when(userRepository.findAll()).thenReturn(users);

        List<User> allUsers = userService.getAll();

        assertThat(allUsers).hasSize(2);
        assertThat(allUsers.get(0).getFirstName()).isEqualTo("Ivan");
        assertThat(allUsers.get(1).getFirstName()).isEqualTo("Petr");
        verify(userRepository).findAll();
    }

    @Test
    void shouldReturnTrueIfAuthenticationSuccess() {
        String username = USER.getUsername();
        String password = "password12";
        String storedPassword = USER.getPassword();

        when(userRepository.findByUsername(username)).thenReturn(USER);
        when(passwordHandler.isPasswordMatch(password, storedPassword)).thenReturn(true);

        boolean result = userService.isAuthenticate(username, password);

        assertTrue(result);
        verify(userRepository).findByUsername(username);
        verify(passwordHandler).isPasswordMatch(password, storedPassword);
    }

    @Test
    void shouldReturnFalseIfPasswordNotMatch() {
        String username = USER.getUsername();
        String password = "password12-not-correct";
        String storedPassword = USER.getPassword();

        when(userRepository.findByUsername(username)).thenReturn(USER);
        when(passwordHandler.isPasswordMatch(password, storedPassword)).thenReturn(false);

        boolean result = userService.isAuthenticate(username, password);

        assertFalse(result);
        verify(userRepository).findByUsername(username);
        verify(passwordHandler).isPasswordMatch(password, storedPassword);
    }

    @Test
    void shouldReturnFalseIfUserNotFound() {
        String username = "not-existed-username";
        String password = "password12";

        when(userRepository.findByUsername(username)).thenReturn(null);
        when(passwordHandler.isPasswordMatch(password, "")).thenReturn(false);

        boolean result = userService.isAuthenticate(username, password);

        assertFalse(result);
        verify(userRepository).findByUsername(username);
        verify(passwordHandler).isPasswordMatch(password, "");
    }

    @Test
    void shouldSetActive() {
        User user = USER.toBuilder().isActive(false).build();
        boolean isActive = true;
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        userService.activateUser(user.getUsername(), isActive);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldNoUpdateIfSameStatus() {
        User user = USER.toBuilder().isActive(true).build();
        boolean isActive = true;
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        userService.activateUser(user.getUsername(), isActive);

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldChangePassword() {
        User user = new User();
        String newPassword = "newPassword";
        String hashedPassword = "hashedPassword";

        when(passwordHandler.hashPassword(newPassword)).thenReturn(hashedPassword);

        userService.changePassword(user, newPassword);

        verify(passwordHandler).hashPassword(newPassword);
        verify(userRepository).save(any(User.class));
    }

}