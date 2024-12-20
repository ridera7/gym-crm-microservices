package com.gym.crm.application.service.impl;

import com.gym.crm.application.dto.client.TrainerWorkloadRequest;
import com.gym.crm.application.dto.criteria.TrainingsListCriteria;
import com.gym.crm.application.entity.Trainer;
import com.gym.crm.application.entity.Training;
import com.gym.crm.application.exception.ServiceUnavailableException;
import com.gym.crm.application.repository.TrainingRepository;
import com.gym.crm.application.repository.specification.TrainingSpecifications;
import com.gym.crm.application.service.TrainingService;
import com.gym.crm.application.service.impl.validation.EntityValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.gym.crm.application.dto.client.TrainerWorkloadRequest.ActionTypeEnum.ADD;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository repository;
    private final EntityValidator validator;
    private final JmsTemplate jmsTemplate;

    @Override
    @Transactional
    public Training save(Training training) {
        validator.checkTrainingValidation(training);

        training = repository.save(training);

        notifyWorkingHoursService(training, ADD);

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

    @Override
    public void notifyWorkingHoursService(Training training, TrainerWorkloadRequest.ActionTypeEnum action) {
        validator.checkTrainingValidation(training);

        Trainer trainer = training.getTrainer();
        TrainerWorkloadRequest request = TrainerWorkloadRequest.builder()
                .username(trainer.getUser().getUsername())
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .isActive(trainer.getUser().isActive())
                .trainingDate(training.getTrainingDate())
                .trainingDuration(training.getTrainingDuration())
                .actionType(action)
                .build();

        try {
            jmsTemplate.convertAndSend("working.hours.queue", request);
            log.info("Message sent to working.hours.queue: {}", request);
        } catch (Exception e) {
            log.error("Failed to send message to working.hours.queue", e);
            throw new ServiceUnavailableException("Unable to notify Working Hours Service");
        }
    }

}
