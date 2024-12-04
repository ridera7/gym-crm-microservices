package com.gym.crm.application.service;

import com.gym.crm.application.dto.authentication.AuthenticationInfo;
import com.gym.crm.application.entity.Trainee;
import com.gym.crm.application.entity.Trainer;

import java.util.List;
import java.util.Set;

public interface TraineeService {

    AuthenticationInfo createTrainee(Trainee traineeRequest);

    Trainee updateTraineeProfile(String username, Trainee traineeRequest);

    List<Trainer> getTrainersListThatNotAssigned(String traineeUsername);

    List<Trainer> updateTrainersList(String traineeUsername, Set<String> trainersUsernamesList);

    Trainee save(Trainee trainee);

    void update(Trainee trainee);

    void delete(Trainee trainee);

    Trainee findById(long id);

    Trainee findByUsername(String username);

    List<Trainee> getAll();

}
