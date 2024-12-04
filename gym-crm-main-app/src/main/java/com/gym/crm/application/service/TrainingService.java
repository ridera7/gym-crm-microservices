package com.gym.crm.application.service;

import com.gym.crm.application.dto.criteria.TrainingsListCriteria;
import com.gym.crm.application.dto.request.TrainingAddRequest;
import com.gym.crm.application.entity.Training;

import java.util.List;

public interface TrainingService {

    void createTraining(TrainingAddRequest addRequest);

    Training save(Training training);

    Training findById(long id);

    List<Training> findAll();

    List<Training> findByCriteria(TrainingsListCriteria criteriaList);

}
