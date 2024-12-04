package com.gym.crm.application.repository;

import com.gym.crm.application.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    Trainer findByUserUsername(String username);

}
