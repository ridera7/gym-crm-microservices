package com.service.working.hours.controller;

import com.service.working.hours.rest.dto.ErrorResponse;
import com.service.working.hours.rest.dto.TrainerWorkloadRequest;
import com.service.working.hours.rest.dto.TrainerWorkloadResponse;
import com.service.working.hours.service.impl.TrainingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    private final TrainingServiceImpl trainingServiceImpl;

    @PostMapping("/summary")
    public ResponseEntity<?> modifyTrainerWorkload(@RequestBody TrainerWorkloadRequest workloadRequest) {
        try {
            trainingServiceImpl.recordTrainingSession(workloadRequest);
            TrainerWorkloadResponse response = new TrainerWorkloadResponse();
            response.setMessage("Workload updated successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.setError("Error updating workload: " + e.getMessage());
            error.setCode(400);

            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/summary")
    public ResponseEntity<?> getTrainerMonthlySummary(
            @RequestParam String username,
            @RequestParam Integer year,
            @RequestParam Integer month) {
        if (month < 1 || month > 12) {
            ErrorResponse error = new ErrorResponse();
            error.setError("Invalid parameters: month must be between 1 and 12");
            error.setCode(400);

            return ResponseEntity.badRequest().body(error);
        }

        try {
            int totalDuration = trainingServiceImpl.getMonthlyTrainingHours(username, year, month);

            return ResponseEntity.ok(totalDuration);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.setError("Error retrieving summary: " + e.getMessage());
            error.setCode(400);

            return ResponseEntity.badRequest().body(error);
        }
    }
}

