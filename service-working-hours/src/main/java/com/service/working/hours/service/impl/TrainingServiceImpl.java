package com.service.working.hours.service.impl;

import com.service.working.hours.entity.Trainer;
import com.service.working.hours.entity.TrainingRecord;
import com.service.working.hours.exception.ValidationException;
import com.service.working.hours.repository.TrainerRepository;
import com.service.working.hours.repository.TrainingRecordRepository;
import com.service.working.hours.rest.dto.TrainerWorkloadRequest;
import com.service.working.hours.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final TrainerRepository trainerRepository;
    private final TrainingRecordRepository trainingRecordRepository;

    @Override
    public void recordTrainingSession(TrainerWorkloadRequest workloadRequest) {
        int year = workloadRequest.getTrainingDate().getYear();
        int month = workloadRequest.getTrainingDate().getMonthValue();
        int duration = workloadRequest.getTrainingDuration();
        String username = workloadRequest.getUsername();

        trainingRecordRepository.findByTrainerUsernameAndYearAndMonth(username, year, month)
                .ifPresentOrElse(
                        record -> updateWorkload(workloadRequest, record, duration),
                        () -> saveNewTrainingRecord(workloadRequest, year, month)
                );
    }

    @Override
    public int getMonthlyTrainingHours(String username, Integer year, Integer month) {
        validateMonth(month);

        return trainingRecordRepository.findByTrainerUsernameAndYearAndMonth(username, year, month)
                .map(TrainingRecord::getDurationSummary)
                .orElse(0);
    }

    private static void validateMonth(Integer month) {
        if (month < 1 || month > 12) {
            throw  new ValidationException("Invalid parameters: month must be between 1 and 12");
        }
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

    private void saveNewTrainingRecord(TrainerWorkloadRequest workloadRequest, int year, int month) {
        TrainingRecord newRecord = createTrainingRecord(workloadRequest, year, month);
        trainingRecordRepository.save(newRecord);
    }

    private TrainingRecord createTrainingRecord(TrainerWorkloadRequest workloadRequest, int year, int month) {
        Trainer trainer = trainerRepository.findById(workloadRequest.getUsername())
                .orElseGet(() -> createNewTrainer(workloadRequest));

        return TrainingRecord.builder()
                .trainer(trainer)
                .year(year)
                .month(month)
                .durationSummary(workloadRequest.getTrainingDuration())
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

}
