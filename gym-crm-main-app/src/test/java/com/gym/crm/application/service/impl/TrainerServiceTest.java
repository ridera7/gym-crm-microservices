package com.gym.crm.application.service.impl;

import com.gym.crm.application.dto.authentication.AuthenticationInfo;
import com.gym.crm.application.entity.Trainer;
import com.gym.crm.application.entity.User;
import com.gym.crm.application.exception.ValidationException;
import com.gym.crm.application.repository.TrainerRepository;
import com.gym.crm.application.service.impl.serviceutils.PasswordHandler;
import com.gym.crm.application.service.impl.validation.EntityValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.gym.crm.application.testdata.EntityTestData.TRAINER;
import static com.gym.crm.application.testdata.EntityTestData.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private EntityValidator entityValidator;

    @Mock
    private PasswordHandler passwordHandler;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @Test
    @DisplayName("Should create trainer and return with id")
    void shouldSaveTrainerAndReturnWithId() {
        trainerService.save(TRAINER);

        verify(trainerRepository).save(TRAINER);
    }

    @Test
    @DisplayName("Should create trainer")
    void shouldCreateTrainer() {
        User requestUser = User.builder()
                .firstName(USER.getFirstName())
                .lastName(USER.getLastName())
                .build();
        Trainer requestTrainer = Trainer.builder()
                .user(requestUser)
                .specialization(TRAINER.getSpecialization())
                .build();
        when(userService.save(any())).thenReturn(USER);
        when(passwordHandler.generateRandomPassword()).thenReturn("1234567890");
        when(trainerRepository.save(any())).thenReturn(TRAINER);

        AuthenticationInfo response = trainerService.createTrainer(requestTrainer);

        assertEquals(USER.getUsername(), response.getUsername());
        assertEquals("1234567890", response.getPassword());
        verify(trainerRepository).save(any());
    }

    @Test
    @DisplayName("Should throw ValidationException when trainer is null")
    void shouldThrowValidationException_WhenTrainerIsNull() {
        doThrow(new ValidationException("Trainer can not be null")).when(entityValidator).checkTrainerValidation(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> trainerService.save(null));

        assertEquals("Trainer can not be null", exception.getMessage());
        verify(trainerRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update trainer")
    void shouldUpdateTrainer() {
        trainerService.update(TRAINER);

        verify(trainerRepository).save(TRAINER);
    }

    @Test
    @DisplayName("Should update trainer profile")
    void shouldUpdateTrainerProfile() {
        User requestUser = User.builder()
                .firstName(USER.getFirstName())
                .lastName(USER.getLastName())
                .isActive(true).build();
        Trainer requestTrainer = Trainer.builder()
                .user(requestUser)
                .specialization(TRAINER.getSpecialization())
                .build();
        when(trainerRepository.findByUserUsername(any())).thenReturn(TRAINER);

        trainerService.updateTrainerProfile(USER.getUsername(), requestTrainer);

        verify(trainerRepository).save(TRAINER);
    }

    @Test
    @DisplayName("Should return trainer by id")
    void shouldReturnTrainerById() {
        when(trainerRepository.findById(1L)).thenReturn(Optional.ofNullable(TRAINER));

        Trainer actualTrainer = trainerService.findById(1L);

        assertEquals(TRAINER, actualTrainer);
        verify(trainerRepository).findById(1L);
    }

    @Test
    void shouldFindByUsername() {
        String username = "Ivan.Ivanoff";
        Trainer trainer = Trainer.builder().user(USER.toBuilder().username(username).build()).build();

        when(trainerRepository.findByUserUsername(username)).thenReturn(trainer);

        Trainer result = trainerService.findByUsername(username);

        assertEquals(trainer, result);
        verify(trainerRepository).findByUserUsername(username);
    }

    @Test
    @DisplayName("Should return list of trainers")
    void shouldReturnListOfTrainers() {
        List<Trainer> expectedTrainersList = List.of(TRAINER.toBuilder().id(1L).build(), TRAINER.toBuilder().id(2L).build());
        when(trainerRepository.findAll()).thenReturn(expectedTrainersList);

        List<Trainer> actualTrainersList = trainerService.getAll();

        assertEquals(expectedTrainersList, actualTrainersList);
        verify(trainerRepository).findAll();
    }

}