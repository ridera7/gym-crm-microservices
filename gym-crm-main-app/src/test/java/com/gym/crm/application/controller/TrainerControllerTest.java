package com.gym.crm.application.controller;

import com.gym.crm.application.rest.dto.ActivateDeactivateRequest;
import com.gym.crm.application.rest.dto.LoginCredentials;
import com.gym.crm.application.rest.dto.TrainerProfile;
import com.gym.crm.application.rest.dto.TrainerProfileResponse;
import com.gym.crm.application.rest.dto.TrainerTrainingsListItem;
import com.gym.crm.application.rest.dto.TrainerUpdateProfileRequest;
import com.gym.crm.application.rest.dto.UpdateTrainerProfile200Response;
import com.gym.crm.application.service.facade.ServiceFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerControllerTest {

    @Mock
    private ServiceFacade serviceFacade;

    @InjectMocks
    private TrainerController trainerController;

    @Test
    void shouldCreateTrainer() {
        TrainerProfile request = new TrainerProfile();
        LoginCredentials expected = new LoginCredentials("username", "password");
        when(serviceFacade.createTrainer(request)).thenReturn(expected);

        LoginCredentials actual = trainerController.createTrainer(request);

        assertEquals(expected, actual);
        verify(serviceFacade).createTrainer(request);
    }

    @Test
    void shouldGetTrainerProfile() {
        String username = "testUsername";
        TrainerProfileResponse expectedResponse = new TrainerProfileResponse();
        when(serviceFacade.findTrainer(username)).thenReturn(expectedResponse);

        TrainerProfileResponse actualResponse = trainerController.getTrainerProfile(username);

        assertEquals(expectedResponse, actualResponse);
        verify(serviceFacade).findTrainer(username);
    }

    @Test
    void shouldUpdateTrainerProfile() {
        String username = "testUsername";
        TrainerUpdateProfileRequest request = new TrainerUpdateProfileRequest();
        UpdateTrainerProfile200Response expectedResponse = new UpdateTrainerProfile200Response();
        when(serviceFacade.updateTrainerProfile(username, request)).thenReturn(expectedResponse);

        UpdateTrainerProfile200Response actualResponse = trainerController.updateTrainerProfile(username, request);

        assertEquals(expectedResponse, actualResponse);
        verify(serviceFacade).updateTrainerProfile(username, request);
    }

    @Test
    void shouldFindTrainersTrainings() {
        String username = "testUsername";
        LocalDate periodFrom = LocalDate.of(2023, 1, 1);
        LocalDate periodTo = LocalDate.of(2023, 12, 31);
        String traineeName = "traineeName";
        List<TrainerTrainingsListItem> expectedTrainings = List.of(new TrainerTrainingsListItem());
        when(serviceFacade.getTrainerTrainingsList(any())).thenReturn(expectedTrainings);

        List<TrainerTrainingsListItem> actualTrainings = trainerController.findTrainersTrainings(username, periodFrom, periodTo, traineeName);

        assertEquals(expectedTrainings, actualTrainings);
        verify(serviceFacade).getTrainerTrainingsList(any());
    }

    @Test
    void shouldActivateDeactivateTrainer() {
        String username = "testUsername";
        ActivateDeactivateRequest isActive = new ActivateDeactivateRequest();

        trainerController.activateDeactivateTrainer(username, isActive);

        verify(serviceFacade).activateUser(username, isActive);
    }

}