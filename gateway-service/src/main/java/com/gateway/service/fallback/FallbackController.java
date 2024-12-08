package com.gateway.service.fallback;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.String.format;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    private static final String SERVICE_UNAVAILABLE_TEMPLATE =
            "%s is currently unavailable. Please try again later";

    @GetMapping("/gym-crm-main-app")
    public ResponseEntity<?> fallbackGymCrmMainApp() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(format(SERVICE_UNAVAILABLE_TEMPLATE, "Gym Crm Main App"));
    }

    @GetMapping("/service-working-hours")
    public ResponseEntity<?> fallbackServiceWorkingHours() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(format(SERVICE_UNAVAILABLE_TEMPLATE, "Service Working Hours"));
    }
}
