package com.gym.crm.application.repository;

import com.gym.crm.application.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingRepository extends JpaRepository<Training,Long>, JpaSpecificationExecutor<Training> {

}
