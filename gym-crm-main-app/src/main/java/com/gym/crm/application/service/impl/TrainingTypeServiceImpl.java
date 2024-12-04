package com.gym.crm.application.service.impl;

import com.gym.crm.application.entity.TrainingType;
import com.gym.crm.application.repository.TrainingTypeRepository;
import com.gym.crm.application.service.TrainingTypeService;
import com.gym.crm.application.service.impl.validation.EntityValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private final TrainingTypeRepository repository;
    private final EntityValidator validator;

    @Override
    @Transactional(readOnly = true)
    public TrainingType findById(Long id) {
        validator.validateEntityId(id);

        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public TrainingType findByName(String name) {
        return repository.findByTrainingTypeName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingType> getAll() {
        return repository.findAll();
    }
}
