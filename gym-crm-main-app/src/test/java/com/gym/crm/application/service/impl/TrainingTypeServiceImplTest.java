package com.gym.crm.application.service.impl;

import com.gym.crm.application.entity.TrainingType;
import com.gym.crm.application.repository.TrainingTypeRepository;
import com.gym.crm.application.service.impl.validation.EntityValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingTypeServiceImplTest {

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private EntityValidator validator;

    @InjectMocks
    private TrainingTypeServiceImpl trainingTypeService;

    @Test
    void shouldFindById() {
        Long id = 1L;
        TrainingType trainingType = TrainingType.builder().id(id).build();

        when(trainingTypeRepository.findById(id)).thenReturn(Optional.ofNullable(trainingType));

        TrainingType result = trainingTypeService.findById(id);

        assertEquals(trainingType, result);
        verify(validator).validateEntityId(id);
        verify(trainingTypeRepository).findById(id);
    }

    @Test
    void shouldFindByName() {
        String name = "Zumba";
        TrainingType trainingType = TrainingType.builder().trainingTypeName(name).build();

        when(trainingTypeRepository.findByTrainingTypeName(name)).thenReturn(trainingType);

        TrainingType result = trainingTypeService.findByName(name);

        assertEquals(trainingType, result);
        verify(trainingTypeRepository).findByTrainingTypeName(name);
    }

    @Test
    void shouldGetAll() {
        TrainingType type1 = new TrainingType();
        TrainingType type2 = new TrainingType();
        List<TrainingType> trainingTypes = Arrays.asList(type1, type2);

        when(trainingTypeRepository.findAll()).thenReturn(trainingTypes);

        List<TrainingType> result = trainingTypeService.getAll();

        assertEquals(trainingTypes, result);
        verify(trainingTypeRepository).findAll();
    }

}