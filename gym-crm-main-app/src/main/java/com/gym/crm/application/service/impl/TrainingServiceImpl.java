package com.gym.crm.application.service.impl;

import com.gym.crm.application.dto.criteria.TrainingsListCriteria;
import com.gym.crm.application.dto.request.TrainingAddRequest;
import com.gym.crm.application.entity.Trainee;
import com.gym.crm.application.entity.Trainer;
import com.gym.crm.application.entity.Training;
import com.gym.crm.application.entity.TrainingType;
import com.gym.crm.application.exception.ValidationException;
import com.gym.crm.application.repository.TrainingRepository;
import com.gym.crm.application.repository.specification.TrainingSpecifications;
import com.gym.crm.application.service.TraineeService;
import com.gym.crm.application.service.TrainerService;
import com.gym.crm.application.service.TrainingService;
import com.gym.crm.application.service.impl.validation.EntityValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.gym.crm.application.constant.Message.NO_USER_WITH_SUCH_USERNAME;
import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository repository;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final EntityValidator validator;

    @Override
    @Transactional
    public void createTraining(TrainingAddRequest addRequest) {
        Trainee trainee = Optional.ofNullable(traineeService.findByUsername(addRequest.getTraineeUsername()))
                .orElseThrow(() -> new ValidationException(
                        format(NO_USER_WITH_SUCH_USERNAME, "trainee", addRequest.getTraineeUsername()))
                );
        Trainer trainer = Optional.ofNullable(trainerService.findByUsername(addRequest.getTrainerUsername()))
                .orElseThrow(() -> new ValidationException(
                        format(NO_USER_WITH_SUCH_USERNAME, "trainer", addRequest.getTrainerUsername()))
                );
        TrainingType trainingType = trainer.getSpecialization();

        repository.save(Training.builder()
                .trainee(trainee)
                .trainer(trainer)
                .trainingDate(addRequest.getTrainingDate())
                .trainingName(addRequest.getTrainingName())
                .trainingType(trainingType)
                .trainingDuration(addRequest.getTrainingDuration())
                .build());
    }

    @Override
    @Transactional
    public Training save(Training training) {
        validator.checkTrainingValidation(training);

        repository.save(training);

        log.info("New training {} saved", training);

        return training;
    }

    @Override
    @Transactional(readOnly = true)
    public Training findById(long id) {
        validator.validateEntityId(id);

        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> findByCriteria(TrainingsListCriteria criteriaList) {
        return repository.findAll(TrainingSpecifications.byCriteria(criteriaList));
    }
}
