package com.service.working.hours.controller;

import com.service.working.hours.rest.dto.ErrorResponse;
import com.service.working.hours.rest.dto.TrainerWorkloadRequest;
import com.service.working.hours.rest.dto.TrainerWorkloadResponse;
import com.service.working.hours.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static com.service.working.hours.rest.dto.TrainerWorkloadRequest.ActionTypeEnum.ADD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerControllerTest {

    @InjectMocks
    private TrainerController trainerController;

    @Mock
    private TrainingService trainingService;

    @Test
    void shouldReturnSuccessResponse_WhenTrainingServiceExecutesSuccessfully() {
        TrainerWorkloadRequest request = new TrainerWorkloadRequest();
        request.setUsername("trainer1");
        request.setTrainingDate(java.time.LocalDate.of(2024, 12, 1));
        request.setTrainingDuration(2);
        request.setActionType(ADD);

        doNothing().when(trainingService).recordTrainingSession(request);

        ResponseEntity<?> response = trainerController.modifyTrainerWorkload(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());

        TrainerWorkloadResponse body = (TrainerWorkloadResponse) response.getBody();
        assertNotNull(body);
        assertEquals("Workload updated successfully", body.getMessage());
    }

    @Test
    void shouldReturnErrorResponse_WhenExceptionOccurs() {
        TrainerWorkloadRequest request = new TrainerWorkloadRequest();
        request.setUsername("trainer1");
        request.setTrainingDate(java.time.LocalDate.of(2024, 12, 1));
        request.setTrainingDuration(2);
        request.setActionType(ADD);

        doThrow(new RuntimeException("Some error occurred")).when(trainingService).recordTrainingSession(request);

        ResponseEntity<?> response = trainerController.modifyTrainerWorkload(request);

        assertNotNull(response);
        assertEquals(400, response.getStatusCode().value());

        ErrorResponse body = (ErrorResponse) response.getBody();
        assertNotNull(body);
        assertEquals("Error updating workload: Some error occurred", body.getError());
        assertEquals(400, body.getCode());
    }

    @Test
    void shouldReturnTotalDuration_WhenParametersAreValid() {
        String username = "trainer1";
        int year = 2024;
        int month = 12;
        int expectedDuration = 10;

        when(trainingService.getMonthlyTrainingHours(username, year, month)).thenReturn(expectedDuration);

        ResponseEntity<?> response = trainerController.getTrainerMonthlySummary(username, year, month);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedDuration, response.getBody());
    }

    @Test
    void shouldReturnErrorResponse_WhenMonthIsInvalid() {
        String username = "trainer1";
        int year = 2024;
        int month = 13;

        ResponseEntity<?> response = trainerController.getTrainerMonthlySummary(username, year, month);

        assertNotNull(response);
        assertEquals(400, response.getStatusCode().value());

        ErrorResponse body = (ErrorResponse) response.getBody();
        assertNotNull(body);
        assertEquals("Invalid parameters: month must be between 1 and 12", body.getError());
        assertEquals(400, body.getCode());
    }

    @Test
    void shouldReturnErrorResponse_WhenExceptionOccursInTrainerMonthlySummary() {
        String username = "trainer1";
        int year = 2024;
        int month = 12;

        when(trainingService.getMonthlyTrainingHours(username, year, month))
                .thenThrow(new RuntimeException("Some error occurred"));

        ResponseEntity<?> response = trainerController.getTrainerMonthlySummary(username, year, month);

        assertNotNull(response);
        assertEquals(400, response.getStatusCode().value());

        ErrorResponse body = (ErrorResponse) response.getBody();
        assertNotNull(body);
        assertEquals("Error retrieving summary: Some error occurred", body.getError());
        assertEquals(400, body.getCode());
    }

}