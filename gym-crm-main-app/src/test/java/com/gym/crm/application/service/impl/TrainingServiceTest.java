package com.gym.crm.application.service.impl;

import com.gym.crm.application.dto.request.TrainingAddRequest;
import com.gym.crm.application.entity.Training;
import com.gym.crm.application.exception.ValidationException;
import com.gym.crm.application.repository.TrainingRepository;
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

import static com.gym.crm.application.testdata.EntityTestData.TRAINEE;
import static com.gym.crm.application.testdata.EntityTestData.TRAINER;
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
    private TraineeServiceImpl traineeService;

    @Mock
    private TrainerServiceImpl trainerService;

    @Mock
    private EntityValidator entityValidator;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @Test
    @DisplayName("Should create training and return with id")
    void shouldSaveTrainingAndReturnWithId() {
        trainingService.save(TRANSIENT_TRAINING);

        verify(trainingRepository).save(TRANSIENT_TRAINING);
    }

    @Test
    void shouldCreateTrainingSuccess() {
        TrainingAddRequest addRequest = new TrainingAddRequest(
                TRAINEE.getUser().getUsername(),
                TRAINER.getUser().getUsername(),
                "Yoga Session",
                LocalDate.now(),
                60
        );

        when(traineeService.findByUsername(TRAINEE.getUser().getUsername())).thenReturn(TRAINEE);
        when(trainerService.findByUsername(TRAINER.getUser().getUsername())).thenReturn(TRAINER);

        trainingService.createTraining(addRequest);

        verify(traineeService).findByUsername(TRAINEE.getUser().getUsername());
        verify(trainerService).findByUsername(TRAINER.getUser().getUsername());
        verify(trainingRepository).save(any(Training.class));
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

}