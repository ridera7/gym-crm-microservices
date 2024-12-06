package com.gym.crm.application.dto.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TrainerWorkloadRequest {

    private String username;

    private String firstName;

    private String lastName;

    private Boolean isActive;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate trainingDate;

    private Integer trainingDuration;

    public enum ActionTypeEnum {
        ADD("ADD"),

        DELETE("DELETE");

        private final String value;

        ActionTypeEnum(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static ActionTypeEnum fromValue(String value) {
            for (ActionTypeEnum b : ActionTypeEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    private ActionTypeEnum actionType;

}
