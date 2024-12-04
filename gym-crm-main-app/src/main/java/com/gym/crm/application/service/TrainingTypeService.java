package com.gym.crm.application.service;

import com.gym.crm.application.entity.TrainingType;

import java.util.List;

public interface TrainingTypeService {

    TrainingType findById(Long id);

    TrainingType findByName(String name);

    List<TrainingType> getAll();

}
