package com.gym.crm.application.service.impl;

import com.gym.crm.application.dto.authentication.AuthenticationInfo;
import com.gym.crm.application.entity.Trainee;
import com.gym.crm.application.entity.Trainer;
import com.gym.crm.application.entity.User;
import com.gym.crm.application.exception.ValidationException;
import com.gym.crm.application.repository.TraineeRepository;
import com.gym.crm.application.service.impl.serviceutils.PasswordHandler;
import com.gym.crm.application.service.impl.validation.EntityValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.gym.crm.application.testdata.EntityTestData.TRAINEE;
import static com.gym.crm.application.testdata.EntityTestData.TRAINER;
import static com.gym.crm.application.testdata.EntityTestData.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private EntityValidator entityValidator;

    @Mock
    private PasswordHandler passwordHandler;

    @Mock
    private TrainerServiceImpl trainerService;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @Test
    @DisplayName("Should call save trainee")
    void shouldSaveTraineeAndReturnWithId() {
        traineeService.save(TRAINEE);

        verify(traineeRepository).save(TRAINEE);
    }

    @Test
    @DisplayName("Should throw ValidationException when trainee is null")
    void shouldThrowValidationException_WhenTraineeIsNull() {
        doThrow(new ValidationException("Trainee can not be null"))
                .when(entityValidator).checkTraineeValidation(null);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> traineeService.save(null));

        assertEquals("Trainee can not be null", exception.getMessage());
        verify(traineeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete trainee")
    void shouldDeleteTrainee() {
        doNothing().when(traineeRepository).deleteById(TRAINEE.getId());

        traineeService.delete(TRAINEE);

        verify(traineeRepository).deleteById(TRAINEE.getId());
    }

    @Test
    @DisplayName("Should return trainee by id")
    void shouldReturnTraineeById() {
        when(traineeRepository.findById(1L)).thenReturn(Optional.ofNullable(TRAINEE));

        Trainee actualTrainee = traineeService.findById(1L);

        assertEquals(TRAINEE, actualTrainee);
        verify(traineeRepository).findById(1L);
    }

    @Test
    void shouldFindByUsername() {
        String username = "Ivan.Ivanoff";
        Trainee trainee = Trainee.builder().user(USER.toBuilder().username(username).build()).build();

        when(traineeRepository.findByUserUsername(username)).thenReturn(trainee);

        Trainee result = traineeService.findByUsername(username);

        assertEquals(trainee, result);
        verify(traineeRepository).findByUserUsername(username);
    }

    @Test
    @DisplayName("Should return list of trainees")
    void shouldReturnListOfTrainees() {
        List<Trainee> expectedTraineesList = List.of(
                TRAINEE.toBuilder().id(1L).build(),
                TRAINEE.toBuilder().id(2L).build()
        );
        when(traineeRepository.findAll()).thenReturn(expectedTraineesList);

        List<Trainee> actualTraineesList = traineeService.getAll();

        assertEquals(expectedTraineesList, actualTraineesList);
        verify(traineeRepository).findAll();
    }

    @Test
    void shouldGetTrainersListThatNotAssigned() {
        String traineeUsername = TRAINEE.getUser().getUsername();

        User inactiveUser = USER.toBuilder().isActive(false).build();
        Trainee trainee = TRAINEE.toBuilder()
                .trainers(Set.of(TRAINER))
                .build();

        Trainer inactiveTrainer = TRAINER.toBuilder().id(2L).user(inactiveUser).build();
        Trainer unassignedActiveTrainer = TRAINER.toBuilder().id(3L).user(USER).build();

        when(traineeRepository.findByUserUsername(traineeUsername)).thenReturn(trainee);
        when(trainerService.getAll()).thenReturn(List.of(TRAINER, inactiveTrainer, unassignedActiveTrainer));

        List<Trainer> result = traineeService.getTrainersListThatNotAssigned(traineeUsername);

        assertEquals(1, result.size());
        assertEquals(USER.getUsername(), result.get(0).getUser().getUsername());

        verify(traineeRepository).findByUserUsername(traineeUsername);
        verify(trainerService).getAll();
    }

    @Test
    @DisplayName("Should create trainee")
    void shouldCreateTrainee() {
        User requestUser = User.builder()
                .firstName(USER.getFirstName())
                .lastName(USER.getLastName())
                .build();
        Trainee requestTrainee = Trainee.builder()
                .user(requestUser)
                .dateOfBirth(LocalDate.of(2000, 11, 6))
                .address("123 street")
                .build();
        when(passwordHandler.generateRandomPassword()).thenReturn("1234567890");
        when(userService.save(any())).thenReturn(USER);

        AuthenticationInfo response = traineeService.createTrainee(requestTrainee);

        assertEquals(USER.getUsername(), response.getUsername());
        assertEquals("1234567890", response.getPassword());
        verify(traineeRepository).save(any());
    }

    @Test
    @DisplayName("Should update trainee")
    void shouldUpdateTrainee() {
        User requestUser = User.builder()
                .firstName("Ivan")
                .lastName("Ivanoff")
                .isActive(true).build();
        Trainee requestTrainee = Trainee.builder()
                .user(requestUser)
                .dateOfBirth(LocalDate.of(2000, 11, 6))
                .address("123 street")
                .build();
        String requestUsername = "Ivan.Ivanoff";
        when(traineeRepository.findByUserUsername(requestUsername)).thenReturn(TRAINEE);

        traineeService.updateTraineeProfile(requestUsername, requestTrainee);

        verify(traineeRepository).save(any());
    }

}