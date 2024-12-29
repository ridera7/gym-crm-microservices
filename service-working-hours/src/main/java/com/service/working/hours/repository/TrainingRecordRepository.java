package com.service.working.hours.repository;

import com.service.working.hours.entity.TrainingRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainingRecordRepository extends MongoRepository<TrainingRecord, String> {
    Optional<TrainingRecord> findByTrainerUsernameAndYearAndMonth(String username, int year, int month);
}
