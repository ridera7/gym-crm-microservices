package com.gym.crm.application.service.impl.validation;

import com.gym.crm.application.entity.User;
import com.gym.crm.application.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.gym.crm.application.testdata.EntityTestData.TRAINEE;
import static com.gym.crm.application.testdata.EntityTestData.TRAINER;
import static com.gym.crm.application.testdata.EntityTestData.TRAINING;
import static com.gym.crm.application.testdata.EntityTestData.USER;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class EntityValidatorTest {

    @InjectMocks
    private EntityValidator validator;

    @Test
    @DisplayName("Should throw exception when user is null")
    void shouldThrowException_WhenUserIsNull() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.checkUserValidation(null));
        assertEquals("User can not be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when user first name is blank")
    void shouldThrowException_WhenUserFirstNameIsBlank() {
        User user = USER.toBuilder().firstName("").build();

        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.checkUserValidation(user));
        assertEquals("User firstName can not be empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when user last name is blank")
    void shouldThrowException_WhenUserLastNameIsBlank() {
        User user = USER.toBuilder().lastName("").build();

        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.checkUserValidation(user));
        assertEquals("User lastName can not be empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when user first name is null")
    void shouldThrowException_WhenUserFirstNameIsNull() {
        User user = USER.toBuilder().firstName(null).build();

        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.checkUserValidation(user));
        assertEquals("User firstName can not be empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when user last name is null")
    void shouldThrowException_WhenUserLastNameIsNull() {
        User user = USER.toBuilder().lastName(null).build();

        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.checkUserValidation(user));
        assertEquals("User lastName can not be empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should not throw exception when user is valid")
    void shouldNotThrowException_WhenUserIsValid() {
        assertDoesNotThrow(() -> validator.checkUserValidation(USER));
    }

    @Test
    @DisplayName("Should throw exception when trainee is null")
    void shouldThrowException_WhenTraineeIsNull() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.checkTraineeValidation(null));
        assertEquals("Trainee can not be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should not throw exception when trainee is valid")
    void shouldNotThrowException_WhenTraineeIsValid() {
        assertDoesNotThrow(() -> validator.checkTraineeValidation(TRAINEE));
    }

    @Test
    @DisplayName("Should throw exception when trainer is null")
    void shouldThrowException_WhenTrainerIsNull() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.checkTrainerValidation(null));
        assertEquals("Trainer can not be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should not throw exception when trainer is valid")
    void shouldNotThrowException_WhenTrainerIsValid() {
        assertDoesNotThrow(() -> validator.checkTrainerValidation(TRAINER));
    }

    @Test
    @DisplayName("Should throw exception when training is null")
    void shouldThrowException_WhenTrainingIsNull() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.checkTrainingValidation(null));
        assertEquals("Training can not be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should not throw exception when training is valid")
    void shouldNotThrowException_WhenTrainingIsValid() {
        assertDoesNotThrow(() -> validator.checkTrainingValidation(TRAINING));
    }

    @Test
    @DisplayName("Should throw exception when id is negative")
    void shouldThrowException_WhenIdIsNegative() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validateEntityId(-1L));
        assertEquals("Id must be positive number", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when id is zero")
    void shouldThrowException_WhenIdIsZero() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validateEntityId(0L));
        assertEquals("Id must be positive number", exception.getMessage());
    }

    @Test
    @DisplayName("Should not throw exception when id is positive")
    void shouldNotThrowException_WhenIdIsPositive() {
        assertDoesNotThrow(() -> validator.validateEntityId(1L));
    }
}