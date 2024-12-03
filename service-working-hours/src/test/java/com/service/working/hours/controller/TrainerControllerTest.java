package com.service.working.hours.controller;

import com.service.working.hours.rest.dto.TrainerWorkloadRequest;
import com.service.working.hours.rest.dto.TrainerWorkloadResponse;
import com.service.working.hours.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static com.service.working.hours.rest.dto.TrainerWorkloadRequest.ActionTypeEnum.ADD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerControllerTest {

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TrainerController trainerController;

    @Test
    void shouldModifyTrainerWorkload() {
        TrainerWorkloadRequest request = new TrainerWorkloadRequest();
        request.setUsername("john.doe");
        request.setTrainingDuration(5);
        request.setTrainingDate(LocalDate.parse("2024-10-15"));
        request.setActionType(ADD);

        doNothing().when(trainingService).recordTrainingSession(request);

        ResponseEntity<?> response = trainerController.modifyTrainerWorkload(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        TrainerWorkloadResponse responseBody = (TrainerWorkloadResponse) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Workload updated successfully", responseBody.getMessage());
        verify(trainingService).recordTrainingSession(request);
    }

    @Test
    void shouldGetTrainerMonthlySummary() {
        String username = "john.doe";
        int year = 2024;
        int month = 12;
        int expectedDuration = 10;

        when(trainingService.getMonthlyTrainingHours(username, year, month)).thenReturn(expectedDuration);

        ResponseEntity<?> response = trainerController.getTrainerMonthlySummary(username, year, month);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedDuration, response.getBody());
        verify(trainingService).getMonthlyTrainingHours(username, year, month);
    }

}