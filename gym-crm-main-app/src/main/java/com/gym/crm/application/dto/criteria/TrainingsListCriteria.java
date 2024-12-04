package com.gym.crm.application.dto.criteria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingsListCriteria {

    private String traineeUsername;
    private String trainerUsername;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String trainingTypeName;

}
