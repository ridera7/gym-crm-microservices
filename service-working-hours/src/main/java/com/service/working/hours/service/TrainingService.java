package com.service.working.hours.service;

import com.service.working.hours.rest.dto.TrainerWorkloadRequest;

public interface TrainingService {

    void recordTrainingSession(TrainerWorkloadRequest workloadRequest);

    int getMonthlyTrainingHours(String username, Integer year, Integer month);

    }
