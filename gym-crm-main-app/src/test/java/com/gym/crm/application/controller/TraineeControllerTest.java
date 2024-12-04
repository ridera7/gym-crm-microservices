package com.gym.crm.application.controller;

import com.gym.crm.application.rest.dto.ActivateDeactivateRequest;
import com.gym.crm.application.rest.dto.LoginCredentials;
import com.gym.crm.application.rest.dto.TraineeProfile;
import com.gym.crm.application.rest.dto.TraineeProfileResponse;
import com.gym.crm.application.rest.dto.TraineeTrainerListUpdateRequestInner;
import com.gym.crm.application.rest.dto.TraineeTrainingsListItem;
import com.gym.crm.application.rest.dto.TraineeUpdateProfileRequest;
import com.gym.crm.application.rest.dto.TrainerItem;
import com.gym.crm.application.rest.dto.UpdateTraineeProfile200Response;
import com.gym.crm.application.service.facade.ServiceFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeControllerTest {

    @Mock
    private ServiceFacade serviceFacade;

    @InjectMocks
    private TraineeController traineeController;

    @Test
    void shouldCreateTrainee() {
        TraineeProfile request = new TraineeProfile();
        LoginCredentials expected = new LoginCredentials("username", "password");
        when(serviceFacade.createTrainee(request)).thenReturn(expected);

        LoginCredentials actual = traineeController.createTrainee(request);

        assertEquals(expected, actual);
        verify(serviceFacade).createTrainee(request);
    }

    @Test
    void shouldGetTraineeProfile() {
        String username = "testUsername";
        TraineeProfileResponse expectedResponse = new TraineeProfileResponse();
        when(serviceFacade.findTrainee(username)).thenReturn(expectedResponse);

        TraineeProfileResponse actualResponse = traineeController.getTraineeProfile(username);

        assertEquals(expectedResponse, actualResponse);
        verify(serviceFacade).findTrainee(username);
    }

    @Test
    void shouldUpdateTraineeProfile() {
        String username = "testUsername";
        TraineeUpdateProfileRequest request = new TraineeUpdateProfileRequest();
        UpdateTraineeProfile200Response expectedResponse = new UpdateTraineeProfile200Response();
        when(serviceFacade.updateTraineeProfile(username, request)).thenReturn(expectedResponse);

        UpdateTraineeProfile200Response actualResponse = traineeController.updateTraineeProfile(username, request);

        assertEquals(expectedResponse, actualResponse);
        verify(serviceFacade).updateTraineeProfile(username, request);
    }

    @Test
    void shouldDeleteTrainee() {
        String username = "testUsername";

        traineeController.deleteTrainee(username);

        verify(serviceFacade).deleteTrainee(username);
    }

    @Test
    void shouldGetActiveTrainers() {
        String username = "testUsername";
        List<TrainerItem> expectedTrainers = List.of(new TrainerItem());
        when(serviceFacade.getTrainersListThatNotAssigned(username)).thenReturn(expectedTrainers);

        List<TrainerItem> actualTrainers = traineeController.getActiveTrainers(username);

        assertEquals(expectedTrainers, actualTrainers);
        verify(serviceFacade).getTrainersListThatNotAssigned(username);
    }

    @Test
    void shouldUpdateTrainersList() {
        String username = "testUsername";
        Set<TraineeTrainerListUpdateRequestInner> trainerList = Set.of(new TraineeTrainerListUpdateRequestInner());
        List<TrainerItem> expectedTrainers = List.of(new TrainerItem());
        when(serviceFacade.updateTrainersList(username, trainerList)).thenReturn(expectedTrainers);

        List<TrainerItem> actualTrainers = traineeController.updateTrainersList(username, trainerList);

        assertEquals(expectedTrainers, actualTrainers);
        verify(serviceFacade).updateTrainersList(username, trainerList);
    }

    @Test
    void shouldFindTraineesTrainings() {
        String username = "testUsername";
        LocalDate periodFrom = LocalDate.of(2023, 1, 1);
        LocalDate periodTo = LocalDate.of(2023, 12, 31);
        String trainerName = "trainerName";
        String trainingType = "trainingType";
        List<TraineeTrainingsListItem> expectedTrainings = List.of(new TraineeTrainingsListItem());
        when(serviceFacade.getTraineeTrainingsList(any())).thenReturn(expectedTrainings);

        List<TraineeTrainingsListItem> actualTrainings = traineeController.findTraineesTrainings(username, periodFrom, periodTo, trainerName, trainingType);

        assertEquals(expectedTrainings, actualTrainings);
        verify(serviceFacade).getTraineeTrainingsList(any());
    }

    @Test
    void shouldActivateDeactivateTrainee() {
        String username = "testUsername";
        ActivateDeactivateRequest isActive = new ActivateDeactivateRequest();

        traineeController.activateDeactivateTrainee(username, isActive);

        verify(serviceFacade).activateUser(username, isActive);
    }
}