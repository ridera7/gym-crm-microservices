package com.gym.crm.application.service.impl;

import com.gym.crm.application.dto.client.TrainerWorkloadRequest;
import com.gym.crm.application.entity.Trainee;
import com.gym.crm.application.entity.Trainer;
import com.gym.crm.application.entity.Training;
import com.gym.crm.application.entity.TrainingType;
import com.gym.crm.application.entity.User;
import com.gym.crm.application.exception.ValidationException;
import com.gym.crm.application.feign.client.WorkingHoursClient;
import com.gym.crm.application.repository.TrainingRepository;
import com.gym.crm.application.service.impl.validation.EntityValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.gym.crm.application.testdata.EntityTestData.TRAINING;
import static com.gym.crm.application.testdata.EntityTestData.TRANSIENT_TRAINING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private EntityValidator entityValidator;

    @Mock
    private WorkingHoursClient workingHoursClient;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @Test
    @DisplayName("Should create training")
    void shouldSaveTraining() {
        when(trainingRepository.save(TRANSIENT_TRAINING)).thenReturn(TRANSIENT_TRAINING);
        when(workingHoursClient.modifyTrainerWorkload(any(TrainerWorkloadRequest.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        trainingService.save(TRANSIENT_TRAINING);

        verify(trainingRepository).save(TRANSIENT_TRAINING);
        verify(workingHoursClient).modifyTrainerWorkload(any());
    }

    @Test
    @DisplayName("Should throw ValidationException when training is null")
    void shouldThrowValidationException_WhenTrainingIsNull() {
        doThrow(new ValidationException("Training can not be null"))
                .when(entityValidator).checkTrainingValidation(null);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> trainingService.save(null));

        assertEquals("Training can not be null", exception.getMessage());
        verify(trainingRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should return training by id")
    void shouldReturnTrainingById() {
        when(trainingRepository.findById(1L)).thenReturn(Optional.ofNullable(TRAINING));

        Training actualTraining = trainingService.findById(1L);

        assertEquals(TRAINING, actualTraining);
        verify(trainingRepository).findById(1L);
    }

    @Test
    @DisplayName("Should return list of trainings")
    void shouldReturnListOfTrainings() {
        List<Training> expectedTrainingsList = List.of(
                TRAINING.toBuilder().id(1L).build(),
                TRAINING.toBuilder().id(2L).build()
        );
        when(trainingRepository.findAll()).thenReturn(expectedTrainingsList);

        List<Training> actualTrainingsList = trainingService.findAll();

        assertEquals(expectedTrainingsList, actualTrainingsList);
        verify(trainingRepository).findAll();
    }

    @Test
    void shouldNotifyWorkingHoursServiceWithCorrectRequest() {
        Training training = createMockTraining();
        TrainerWorkloadRequest.ActionTypeEnum action = TrainerWorkloadRequest.ActionTypeEnum.ADD;
        when(workingHoursClient.modifyTrainerWorkload(any(TrainerWorkloadRequest.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        trainingService.notifyWorkingHoursService(training, action);

        ArgumentCaptor<TrainerWorkloadRequest> captor = ArgumentCaptor.forClass(TrainerWorkloadRequest.class);
        verify(workingHoursClient).modifyTrainerWorkload(captor.capture());
        TrainerWorkloadRequest capturedRequest = captor.getValue();

        assertEquals(training.getTrainer().getUser().getUsername(), capturedRequest.getUsername());
        assertEquals(training.getTrainer().getUser().getFirstName(), capturedRequest.getFirstName());
        assertEquals(training.getTrainer().getUser().getLastName(), capturedRequest.getLastName());
        assertEquals(training.getTrainer().getUser().isActive(), capturedRequest.getIsActive());
        assertEquals(training.getTrainingDate(), capturedRequest.getTrainingDate());
        assertEquals(training.getTrainingDuration(), capturedRequest.getTrainingDuration());
        assertEquals(action, capturedRequest.getActionType());
    }

    private Training createMockTraining() {
        TrainingType trainingType = new TrainingType(1L, "Zumba");
        User userTrainer = new User().toBuilder()
                .username("Taras.Tarasoff")
                .firstName("Taras")
                .lastName("Tarasoff")
                .isActive(true)
                .build();
        User userTrainee = new User().toBuilder()
                .username("Ivan.Ivanoff")
                .firstName("Ivan")
                .lastName("Ivanoff")
                .isActive(true)
                .build();
        Trainer trainer = new Trainer().toBuilder()
                .user(userTrainer)
                .specialization(trainingType)
                .build();
        Trainee trainee = new Trainee().toBuilder()
                .user(userTrainee)
                .dateOfBirth(LocalDate.of(2000, 11, 6))
                .address("Some address")
                .build();

        return new Training().toBuilder()
                .trainer(trainer)
                .trainee(trainee)
                .trainingName("Morning motion")
                .trainingType(trainingType)
                .trainingDate(LocalDate.of(2024, 12, 5))
                .trainingDuration(60)
                .build();
    }

}