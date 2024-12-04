package com.gym.crm.application.service.impl;

import com.gym.crm.application.dto.authentication.AuthenticationInfo;
import com.gym.crm.application.entity.Trainer;
import com.gym.crm.application.entity.TrainingType;
import com.gym.crm.application.entity.User;
import com.gym.crm.application.exception.NotFoundException;
import com.gym.crm.application.repository.TrainerRepository;
import com.gym.crm.application.service.TrainerService;
import com.gym.crm.application.service.UserService;
import com.gym.crm.application.service.impl.serviceutils.PasswordHandler;
import com.gym.crm.application.service.impl.validation.EntityValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository repository;
    private final UserService userService;
    private final EntityValidator validator;
    private final PasswordHandler passwordHandler;

    @Override
    @Transactional
    public AuthenticationInfo createTrainer(Trainer trainerRequest) {
        String newPassword = passwordHandler.generateRandomPassword();
        TrainingType specialization = trainerRequest.getSpecialization();

        User newUser = userService.save(trainerRequest.getUser()
                .toBuilder().password(newPassword).build());

        repository.save(trainerRequest.toBuilder()
                .id(newUser.getId())
                .specialization(specialization)
                .user(newUser).build());

        return new AuthenticationInfo(newUser.getUsername(), newPassword);
    }

    @Override
    @Transactional
    public Trainer updateTrainerProfile(String username, Trainer trainerRequest) {
        Trainer trainer = repository.findByUserUsername(username);
        TrainingType specialization = trainer.getSpecialization();

        User user = trainerRequest.getUser().toBuilder()
                .id(trainer.getUser().getId())
                .username(trainer.getUser().getUsername())
                .password(trainer.getUser().getPassword())
                .isActive(Optional.of(trainerRequest.getUser().isActive())
                        .orElse(trainer.getUser().isActive()))
                .build();
        userService.update(user);

        Trainer newTrainer = Trainer.builder()
                .id(trainer.getId())
                .specialization(specialization)
                .user(user)
                .trainees(trainer.getTrainees())
                .build();
        repository.save(newTrainer);

        return newTrainer;
    }

    @Override
    public Trainer save(Trainer trainer) {
        validator.checkTrainerValidation(trainer);

        repository.save(trainer);

        log.info("New trainer (Id = {}) saved", trainer.getId());

        return trainer;
    }

    @Override
    public void update(Trainer trainer) {
        validator.validateEntityId(trainer.getId());

        repository.save(trainer);
    }

    @Override
    public Trainer findById(long id) {
        validator.validateEntityId(id);
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Trainer findByUsername(String username) {
        return Optional.ofNullable(repository.findByUserUsername(username))
                .orElseThrow(() -> new NotFoundException(format("Trainer (%s) not found", username)));
    }

    @Override
    public List<Trainer> getAll() {
        return repository.findAll();
    }

}
