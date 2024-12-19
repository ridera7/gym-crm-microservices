package com.service.working.hours.listener;

import com.service.working.hours.exception.DeadMessageException;
import com.service.working.hours.rest.dto.TrainerWorkloadRequest;
import com.service.working.hours.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TrainerWorkloadListenerTest {

    @InjectMocks
    private TrainerWorkloadListener listener;

    @Mock
    private TrainingService trainingService;

    @Mock
    private TrainerWorkloadRequest workloadRequest;

    @Test
    void shouldProcessTrainerWorkloadSuccess() {
        doNothing().when(trainingService).recordTrainingSession(workloadRequest);

        listener.processTrainerWorkload(workloadRequest);

        verify(trainingService).recordTrainingSession(workloadRequest);
    }

    @Test
    void shouldThrowExceptionProcessTrainerWorkload_ExceptionThrown() {
        RuntimeException mockException = new RuntimeException("Error");
        doThrow(mockException).when(trainingService).recordTrainingSession(workloadRequest);

        DeadMessageException exception = assertThrows(
                DeadMessageException.class,
                () -> listener.processTrainerWorkload(workloadRequest)
        );

        assertEquals("Error", exception.getMessage());
        verify(trainingService).recordTrainingSession(workloadRequest);
    }
}