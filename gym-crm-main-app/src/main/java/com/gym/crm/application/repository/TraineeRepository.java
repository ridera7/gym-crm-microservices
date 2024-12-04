package com.gym.crm.application.repository;

import com.gym.crm.application.entity.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Long> {

    Trainee findByUserUsername(String username);

}