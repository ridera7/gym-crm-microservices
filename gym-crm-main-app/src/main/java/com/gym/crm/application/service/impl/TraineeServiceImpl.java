package com.gym.crm.application.service.impl;

import com.gym.crm.application.dto.authentication.AuthenticationInfo;
import com.gym.crm.application.entity.Trainee;
import com.gym.crm.application.entity.Trainer;
import com.gym.crm.application.entity.User;
import com.gym.crm.application.exception.NotFoundException;
import com.gym.crm.application.repository.TraineeRepository;
import com.gym.crm.application.service.TraineeService;
import com.gym.crm.application.service.TrainerService;
import com.gym.crm.application.service.UserService;
import com.gym.crm.application.service.impl.serviceutils.PasswordHandler;
import com.gym.crm.application.service.impl.validation.EntityValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.lang.String.format;

@Slf4j
@RequiredArgsConstructor
@Service
public class TraineeServiceImpl implements TraineeService {

    private final TraineeRepository repository;
    private final UserService userService;
    private final TrainerService trainerService;
    private final EntityValidator validator;
    private final PasswordHandler passwordHandler;

    @Override
    @Transactional
    public AuthenticationInfo createTrainee(Trainee traineeRequest) {
        String newPassword = passwordHandler.generateRandomPassword();

        User newUser = userService.save(traineeRequest.getUser()
                .toBuilder().password(newPassword).build());

        repository.save(traineeRequest.toBuilder()
                .id(newUser.getId())
                .user(newUser).build());

        return new AuthenticationInfo(newUser.getUsername(), newPassword);
    }

    @Override
    public Trainee save(Trainee trainee) {
        validator.checkTraineeValidation(trainee);

        repository.save(trainee);

        log.info("New trainee {} saved", trainee);

        return trainee;
    }

    @Override
    @Transactional
    public Trainee updateTraineeProfile(String username, Trainee traineeRequest) {
        Trainee trainee = repository.findByUserUsername(username);

        User user = traineeRequest.getUser().toBuilder()
                .id(trainee.getUser().getId())
                .username(trainee.getUser().getUsername())
                .password(trainee.getUser().getPassword())
                .isActive(Optional.of(traineeRequest.getUser().isActive())
                        .orElse(trainee.getUser().isActive()))
                .build();
        userService.update(user);

        Trainee newTrainee = Trainee.builder()
                .id(trainee.getId())
                .dateOfBirth(Optional.ofNullable(traineeRequest.getDateOfBirth())
                        .orElse(trainee.getDateOfBirth()))
                .address(Optional.ofNullable(traineeRequest.getAddress())
                        .orElse(trainee.getAddress()))
                .user(user)
                .trainers(trainee.getTrainers())
                .build();

        repository.save(newTrainee);

        return newTrainee;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> getTrainersListThatNotAssigned(String traineeUsername) {
        Trainee trainee = Optional.ofNullable(repository.findByUserUsername(traineeUsername))
                .orElseThrow(() -> new NotFoundException(format("Trainee '%s' not found", traineeUsername)));
        List<Trainer> assignedTrainers = trainee.getTrainers().stream().toList();
        List<Trainer> allTrainers = trainerService.getAll();

        return allTrainers.stream()
                .filter(trainer -> trainer.getUser().isActive() && !assignedTrainers.contains(trainer))
                .toList();
    }

    @Override
    @Transactional
    public List<Trainer> updateTrainersList(String traineeUsername, Set<String> trainersUsernamesList) {
        Trainee trainee = Optional.ofNullable(repository.findByUserUsername(traineeUsername))
                .orElseThrow(() -> new NotFoundException(format("Trainee '%s' not found", traineeUsername)));

        Set<Trainer> trainers = new HashSet<>();

        for (String trainerUsername: trainersUsernamesList) {
            trainers.add(trainerService.findByUsername(trainerUsername));
        }

        repository.save(trainee.toBuilder()
                .trainers(trainers).build());

        return trainers.stream().toList();
    }

    @Override
    public void update(Trainee trainee) {
        validator.validateEntityId(trainee.getId());

        repository.save(trainee);
    }

    @Override
    public void delete(Trainee trainee) {
        validator.validateEntityId(trainee.getId());

        repository.deleteById(trainee.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Trainee findById(long id) {
        validator.validateEntityId(id);

        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Trainee findByUsername(String username) {

        return Optional.ofNullable(repository.findByUserUsername(username))
                .orElseThrow(() -> new NotFoundException(format("Trainee (%s) not found", username)));
    }

    @Override
    public List<Trainee> getAll() {
        return repository.findAll();
    }

}
