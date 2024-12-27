package com.service.working.hours.entity;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "training_record")
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TrainingRecord {

    @Id
    private String id;

    @DBRef
    private Trainer trainer;

    @Field("training_year")
    private Integer year;

    @Field("training_month")
    private Integer month;

    @Field("duration_summary")
    private Integer durationSummary;

}
