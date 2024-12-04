package com.gym.crm.application.repository.specification;

import com.gym.crm.application.dto.criteria.TrainingsListCriteria;
import com.gym.crm.application.entity.Trainee;
import com.gym.crm.application.entity.Trainer;
import com.gym.crm.application.entity.Training;
import com.gym.crm.application.entity.TrainingType;
import com.gym.crm.application.entity.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TrainingSpecifications {

    public static Specification<Training> byCriteria(TrainingsListCriteria listCriteria) {
        return (root, query, criteriaBuilder) -> {
            Join<Training, Trainee> traineeJoin = root.join("trainee");
            Join<Trainee, User> traineeUserJoin = traineeJoin.join("user");
            Join<Training, Trainer> trainerJoin = root.join("trainer");
            Join<Trainer, User> trainerUserJoin = trainerJoin.join("user");
            Join<Training, TrainingType> trainingTypeJoin = root.join("trainingType");

            List<Predicate> predicates = new ArrayList<>();

            if (listCriteria.getTraineeUsername() != null && !listCriteria.getTraineeUsername().isEmpty()) {
                predicates.add(criteriaBuilder.equal(traineeUserJoin.get("username"), listCriteria.getTraineeUsername()));
            }

            if (listCriteria.getTrainerUsername() != null && !listCriteria.getTrainerUsername().isEmpty()) {
                predicates.add(criteriaBuilder.equal(trainerUserJoin.get("username"), listCriteria.getTrainerUsername()));
            }

            if (listCriteria.getFromDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("trainingDate"), listCriteria.getFromDate()));
            }

            if (listCriteria.getToDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("trainingDate"), listCriteria.getToDate()));
            }

            if (listCriteria.getTrainingTypeName() != null && !listCriteria.getTrainingTypeName().isEmpty()) {
                predicates.add(criteriaBuilder.equal(trainingTypeJoin.get("trainingTypeName"), listCriteria.getTrainingTypeName()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}