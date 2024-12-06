package com.gym.crm.application.service;

import com.gym.crm.application.dto.client.TrainerWorkloadRequest;
import com.gym.crm.application.dto.criteria.TrainingsListCriteria;
import com.gym.crm.application.entity.Training;

import java.util.List;

public interface TrainingService {

    Training save(Training training);

    Training findById(long id);

    List<Training> findAll();

    List<Training> findByCriteria(TrainingsListCriteria criteriaList);

    void notifyWorkingHoursService(Training training, TrainerWorkloadRequest.ActionTypeEnum action);
}
