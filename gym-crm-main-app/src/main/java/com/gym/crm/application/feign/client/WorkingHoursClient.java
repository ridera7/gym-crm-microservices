package com.gym.crm.application.feign.client;

import com.gym.crm.application.dto.client.TrainerWorkloadRequest;
import com.gym.crm.application.feign.config.FeignConfig;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "service-working-hours",
        path = "/api/v1",
        configuration = FeignConfig.class
)
public interface WorkingHoursClient {

    @PostMapping("/trainer/summary")
    @CircuitBreaker(name = "workingHoursClientCircuitBreaker", fallbackMethod = "fallBackMethod")
    ResponseEntity<?> modifyTrainerWorkload(@RequestBody TrainerWorkloadRequest workloadRequest);

    @GetMapping("/trainer/summary")
    @CircuitBreaker(name = "workingHoursClientCircuitBreaker", fallbackMethod = "fallBackMethod")
    ResponseEntity<?> getTrainerMonthlySummary(
            @NonNull @RequestParam String username,
            @NonNull @RequestParam Integer year,
            @NonNull @RequestParam Integer month);

    default ResponseEntity<?> fallBackMethod(Throwable throwable) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }
}
