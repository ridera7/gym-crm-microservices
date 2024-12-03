package com.service.working.hours.service.impl;

import com.service.working.hours.entity.Trainer;
import com.service.working.hours.entity.TrainingRecord;
import com.service.working.hours.repository.TrainerRepository;
import com.service.working.hours.repository.TrainingRecordRepository;
import com.service.working.hours.rest.dto.TrainerWorkloadRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static com.service.working.hours.rest.dto.TrainerWorkloadRequest.ActionTypeEnum.ADD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainingRecordRepository trainingRecordRepository;

    @Test
    void shouldAddTrainingRecord_WhenTrainerExists() {
        TrainerWorkloadRequest request = new TrainerWorkloadRequest();
        request.setUsername("trainer1");
        request.setTrainingDate(LocalDate.of(2024, 12, 1));
        request.setTrainingDuration(2);
        request.setActionType(ADD);

        TrainingRecord existingRecord = TrainingRecord.builder()
                .trainer(Trainer.builder().username("trainer1").build())
                .year(2024)
                .month(12)
                .durationSummary(5)
                .build();

        when(trainingRecordRepository.findByTrainerUsernameAndYearAndMonth("trainer1", 2024, 12))
                .thenReturn(Optional.of(existingRecord));

        trainingService.recordTrainingSession(request);

        ArgumentCaptor<TrainingRecord> captor = ArgumentCaptor.forClass(TrainingRecord.class);
        verify(trainingRecordRepository).save(captor.capture());
        TrainingRecord savedRecord = captor.getValue();

        assertNotNull(savedRecord);
        assertEquals(7, savedRecord.getDurationSummary());
    }

    @Test
    void shouldCreateTrainingRecord_WhenNoExistingRecord() {
        TrainerWorkloadRequest request = new TrainerWorkloadRequest();
        request.setUsername("trainer1");
        request.setTrainingDate(LocalDate.of(2024, 12, 1));
        request.setTrainingDuration(3);
        request.setActionType(ADD);

        Trainer trainer = Trainer.builder()
                .username("trainer1")
                .firstName("John")
                .lastName("Doe")
                .isActive(true)
                .build();

        when(trainingRecordRepository.findByTrainerUsernameAndYearAndMonth("trainer1", 2024, 12))
                .thenReturn(Optional.empty());
        when(trainerRepository.findById("trainer1")).thenReturn(Optional.of(trainer));

        trainingService.recordTrainingSession(request);

        verify(trainingRecordRepository).save(any(TrainingRecord.class));
    }

    @Test
    void shouldReturnTotalDuration() {
        when(trainingRecordRepository.findTotalDurationByTrainerAndYearAndMonth("trainer1", 2024, 12))
                .thenReturn(10);

        int result = trainingService.getMonthlyTrainingHours("trainer1", 2024, 12);

        assertEquals(10, result);
        verify(trainingRecordRepository)
                .findTotalDurationByTrainerAndYearAndMonth("trainer1", 2024, 12);
    }

    @Test
    void shouldCreateNewTrainerAndTrainingRecord_WhenTrainerDoesNotExist() {
        TrainerWorkloadRequest request = new TrainerWorkloadRequest();
        request.setUsername("trainer2");
        request.setFirstName("Alice");
        request.setLastName("Smith");
        request.setIsActive(true);
        request.setTrainingDate(LocalDate.of(2024, 11, 1));
        request.setTrainingDuration(45);
        request.setActionType(ADD);

        when(trainerRepository.findById("trainer2")).thenReturn(Optional.empty());

        trainingService.recordTrainingSession(request);

        verify(trainerRepository).save(any(Trainer.class));
        verify(trainingRecordRepository).save(any(TrainingRecord.class));
    }

}