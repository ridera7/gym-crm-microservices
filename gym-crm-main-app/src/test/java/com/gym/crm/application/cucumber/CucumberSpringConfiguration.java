package com.gym.crm.application.cucumber;

import com.gym.crm.application.GymCRMApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest(classes = {GymCRMApplication.class})
@CucumberContextConfiguration
@Transactional
public class CucumberSpringConfiguration {
}
