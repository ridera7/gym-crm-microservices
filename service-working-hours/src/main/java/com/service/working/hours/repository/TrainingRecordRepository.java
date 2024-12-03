package com.service.working.hours.repository;

import com.service.working.hours.entity.TrainingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingRecordRepository extends JpaRepository<TrainingRecord, Long> {

    List<TrainingRecord> findByTrainerUsername(String trainerUsername);

    @Query("SELECT COALESCE(SUM(tr.durationSummary), 0) " +
            "FROM TrainingRecord tr " +
            "WHERE tr.trainer.username = :trainerUsername " +
            "AND tr.year = :year " +
            "AND tr.month = :month")
    Integer findTotalDurationByTrainerAndYearAndMonth(@Param("trainerUsername") String trainerUsername,
                                                      @Param("year") Integer year,
                                                      @Param("month") Integer month);

    Optional<TrainingRecord> findByTrainerUsernameAndYearAndMonth(String trainerUsername, Integer year, Integer month);
}
