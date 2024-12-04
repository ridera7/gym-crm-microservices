package com.gym.crm.application.testdata;

import com.gym.crm.application.entity.Trainee;
import com.gym.crm.application.entity.Trainer;
import com.gym.crm.application.entity.Training;
import com.gym.crm.application.entity.TrainingType;
import com.gym.crm.application.entity.User;

import java.time.LocalDate;

public class EntityTestData {

    public static final TrainingType TRAINING_TYPE = createTrainingType();
    public static final User USER = createUser();
    public static final User TRANSIENT_USER = createTransientUser();
    public static final Trainee TRAINEE = createTrainee();
    public static final Trainee TRANSIENT_TRAINEE = createTransientTrainee();
    public static final Trainer TRAINER = createTrainer();
    public static final Trainer TRANSIENT_TRAINER = createTransientTrainer();
    public static final Training TRAINING = createTraining();
    public static final Training TRANSIENT_TRAINING = createTransientTraining();

    private static TrainingType createTrainingType() {
        return new TrainingType(3L, "Zumba");
    }

    private static User createUser() {
        return User.builder().id(1L).firstName("Ivan").lastName("Ivanoff")
                .username("Ivan.Ivanoff")
                .password("1c5ba2b9514fd5c1c22c3fc13389e154:5b8172189eb99ed091cae5b7e043cafa803f54c8e273f52d88abf8461da54e31")
                // password = "password12"
                .isActive(true).build();
    }

    private static User createTransientUser() {
        return createUser().toBuilder().id(null).build();
    }

    private static Trainee createTrainee() {
        return Trainee.builder().id(1L).dateOfBirth(LocalDate.of(2000, 11, 6))
                .address("address").user(USER).build();
    }

    private static Trainee createTransientTrainee() {
        return createTrainee().toBuilder().user(TRANSIENT_USER).build();
    }

    private static Trainer createTrainer() {
        return Trainer.builder().id(1L).specialization(TRAINING_TYPE).user(USER).build();
    }

    private static Trainer createTransientTrainer() {
        return createTrainer().toBuilder().user(TRANSIENT_USER).build();
    }

    private static Training createTraining() {
        return Training.builder().id(1L).trainee(TRAINEE).trainer(TRAINER)
                .trainingName("Cardio").trainingType(TRAINING_TYPE)
                .trainingDate(LocalDate.of(2023, 9, 1))
                .trainingDuration(60).build();
    }

    private static Training createTransientTraining() {
        return createTraining().toBuilder()
                .trainee(TRANSIENT_TRAINEE).trainer(TRANSIENT_TRAINER).build();
    }

}
