package com.gym.crm.application.feign.client;

import com.gym.crm.application.dto.client.TrainerWorkloadRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "service-working-hours", path = "/api/v1")
public interface WorkingHoursClient {

    @PostMapping("/trainer/summary")
    ResponseEntity<?> modifyTrainerWorkload(@RequestBody TrainerWorkloadRequest workloadRequest);

    @GetMapping("/trainer/summary")
    ResponseEntity<?> getTrainerMonthlySummary(
            @NonNull @RequestParam String username,
            @NonNull @RequestParam Integer year,
            @NonNull @RequestParam Integer month);
}
