package com.gym.crm.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GymCRMApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymCRMApplication.class, args);
    }

}
