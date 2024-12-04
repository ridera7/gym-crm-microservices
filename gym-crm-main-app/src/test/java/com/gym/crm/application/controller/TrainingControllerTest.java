package com.gym.crm.application.controller;

import com.gym.crm.application.rest.dto.AddTrainingRequest;
import com.gym.crm.application.service.facade.ServiceFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TrainingControllerTest {

    @Mock
    private ServiceFacade serviceFacade;

    @InjectMocks
    private TrainingController trainingController;

    @Test
    void shouldAddTraining() {
        AddTrainingRequest addTrainingRequest = new AddTrainingRequest();

        trainingController.addTraining(addTrainingRequest);

        verify(serviceFacade).createTraining(addTrainingRequest);
    }

}