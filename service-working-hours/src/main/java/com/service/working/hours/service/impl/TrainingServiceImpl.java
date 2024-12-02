package com.service.working.hours.service.impl;

import com.service.working.hours.entity.Trainer;
import com.service.working.hours.entity.TrainingRecord;
import com.service.working.hours.exception.NotFoundException;
import com.service.working.hours.exception.ValidationException;
import com.service.working.hours.repository.TrainerRepository;
import com.service.working.hours.repository.TrainingRecordRepository;
import com.service.working.hours.rest.dto.TrainerWorkloadRequest;
import com.service.working.hours.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final TrainerRepository trainerRepository;
    private final TrainingRecordRepository trainingRecordRepository;

    @Override
    @Transactional
    public void recordTrainingSession(TrainerWorkloadRequest workloadRequest) {
        int year = workloadRequest.getTrainingDate().getYear();
        int month = workloadRequest.getTrainingDate().getMonthValue();
        int duration = workloadRequest.getTrainingDuration();
        String username = workloadRequest.getUsername();

        TrainingRecord trainingRecord = trainingRecordRepository.findByTrainerUsernameAndYearAndMonth(username, year, month)
                .orElseGet(() -> createTrainingRecord(workloadRequest));
        updateWorkload(workloadRequest, trainingRecord, duration);
    }

    @Override
    public int getMonthlyTrainingHours(String username, Integer year, Integer month) {
        return trainingRecordRepository.findTotalDurationByTrainerAndYearAndMonth(username, year, month);
    }

    private void updateWorkload(TrainerWorkloadRequest workloadRequest, TrainingRecord trainingRecord, int duration) {
        int workload = trainingRecord.getDurationSummary();

        switch (workloadRequest.getActionType().toString().toUpperCase()) {
            case "ADD":
                workload += duration;
                break;
            case "DELETE":
                workload -= duration;
                break;
            default:
                throw new ValidationException("Action is unknown");
        }

        TrainingRecord updateTrainingRecord = trainingRecord.toBuilder()
                .durationSummary(workload).build();

        trainingRecordRepository.save(updateTrainingRecord);
    }

    private TrainingRecord createTrainingRecord(TrainerWorkloadRequest workloadRequest) {
        Trainer trainer = trainerRepository.findById(workloadRequest.getUsername())
                .orElseGet(() -> createNewTrainer(workloadRequest));

        return new TrainingRecord().toBuilder()
                .trainer(trainer)
                .year(workloadRequest.getTrainingDate().getYear())
                .month(workloadRequest.getTrainingDate().getMonthValue())
                .durationSummary(0)
                .build();
    }

    private Trainer createNewTrainer(TrainerWorkloadRequest workloadRequest) {
        Trainer newTrainer = Trainer.builder()
                .username(workloadRequest.getUsername())
                .firstName(workloadRequest.getFirstName())
                .lastName(workloadRequest.getLastName())
                .isActive(workloadRequest.getIsActive())
                .build();
        trainerRepository.save(newTrainer);

        return newTrainer;
    }

    public Trainer saveTrainer(Trainer trainer) {
        return trainerRepository.save(trainer);
    }

    public TrainingRecord saveTrainingRecord(TrainingRecord trainingRecord) {
        return trainingRecordRepository.save(trainingRecord);
    }

    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }

    public Trainer getTrainer(String username) {
        return trainerRepository.findById(username).orElseThrow(() ->
                new NotFoundException("Trainer not found: " + username));
    }

    public Integer getTotalDuration(String username, Integer year, Integer month) {
        return trainingRecordRepository.findTotalDurationByTrainerAndYearAndMonth(username, year, month);
    }
}
