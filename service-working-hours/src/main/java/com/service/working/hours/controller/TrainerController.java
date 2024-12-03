package com.service.working.hours.controller;

import com.service.working.hours.rest.dto.TrainerWorkloadRequest;
import com.service.working.hours.rest.dto.TrainerWorkloadResponse;
import com.service.working.hours.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/trainer")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainingService trainingService;

    @PostMapping("/summary")
    public ResponseEntity<?> modifyTrainerWorkload(@RequestBody TrainerWorkloadRequest workloadRequest) {
        trainingService.recordTrainingSession(workloadRequest);

        return ResponseEntity.ok(new TrainerWorkloadResponse().message("Workload updated successfully"));
    }

    @GetMapping("/summary")
    public ResponseEntity<?> getTrainerMonthlySummary(
            @NonNull @RequestParam String username,
            @NonNull @RequestParam Integer year,
            @NonNull @RequestParam Integer month) {
        int totalDuration = trainingService.getMonthlyTrainingHours(username, year, month);

        return ResponseEntity.ok(totalDuration);
    }
}

