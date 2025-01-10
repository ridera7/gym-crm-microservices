package com.service.working.hours.cucumber;

import com.service.working.hours.ServiceWorkingHoursApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = {ServiceWorkingHoursApplication.class})
@CucumberContextConfiguration
public class CucumberSpringConfiguration {
}
