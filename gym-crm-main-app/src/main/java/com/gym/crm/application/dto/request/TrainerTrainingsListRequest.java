package com.gym.crm.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class TrainerTrainingsListRequest {

    private String username;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String traineeUsername;

}
