package com.gym.crm.application.repository;

import com.gym.crm.application.entity.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long> {

    TrainingType findByTrainingTypeName(String name);

}