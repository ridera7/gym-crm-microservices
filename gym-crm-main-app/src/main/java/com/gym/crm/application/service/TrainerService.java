package com.gym.crm.application.service;

import com.gym.crm.application.dto.authentication.AuthenticationInfo;
import com.gym.crm.application.entity.Trainer;

import java.util.List;

public interface TrainerService {

    AuthenticationInfo createTrainer(Trainer trainerRequest);

    Trainer updateTrainerProfile(String username, Trainer profileRequest);

    Trainer save(Trainer trainer);

    void update(Trainer trainer);

    Trainer findById(long id);

    Trainer findByUsername(String username);

    List<Trainer> getAll();

}
