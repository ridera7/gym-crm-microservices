package com.gym.crm.application.controller;

import com.gym.crm.application.rest.dto.TrainingTypeListResponseInner;
import com.gym.crm.application.service.facade.ServiceFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingTypeControllerTest {

    @Mock
    private ServiceFacade serviceFacade;

    @InjectMocks
    private TrainingTypeController trainingTypeController;

    @Test
    void shouldGetTrainingTypes() {
        List<TrainingTypeListResponseInner> mockResponse = List.of(new TrainingTypeListResponseInner());
        when(serviceFacade.getTrainingTypes()).thenReturn(mockResponse);

        List<TrainingTypeListResponseInner> result = trainingTypeController.getTrainingTypes();

        assertEquals(mockResponse, result);
        verify(serviceFacade).getTrainingTypes();
    }

}