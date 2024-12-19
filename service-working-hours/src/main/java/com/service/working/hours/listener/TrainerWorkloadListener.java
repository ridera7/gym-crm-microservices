package com.service.working.hours.listener;

import com.service.working.hours.exception.DeadMessageException;
import com.service.working.hours.rest.dto.TrainerWorkloadRequest;
import com.service.working.hours.service.TrainingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TrainerWorkloadListener {

    private final TrainingService trainingService;

    public TrainerWorkloadListener(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @JmsListener(destination = "working.hours.queue")
    public void processTrainerWorkload(TrainerWorkloadRequest workloadRequest) {
        try {
            trainingService.recordTrainingSession(workloadRequest);
            log.info("Processed workload request: {}", workloadRequest);
        } catch (Exception e) {
            log.error("Failed to process workload request: {}", workloadRequest, e);
            throw new DeadMessageException(e.getMessage());
        }
    }
}

