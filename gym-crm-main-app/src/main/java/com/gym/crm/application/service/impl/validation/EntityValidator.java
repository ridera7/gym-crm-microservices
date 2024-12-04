package com.gym.crm.application.service.impl.validation;

import com.gym.crm.application.entity.Trainee;
import com.gym.crm.application.entity.Trainer;
import com.gym.crm.application.entity.Training;
import com.gym.crm.application.entity.User;
import com.gym.crm.application.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.gym.crm.application.constant.Message.ENTITY_CANNOT_BE_NULL_ERROR_TEMPLATE;
import static com.gym.crm.application.constant.Message.ID_CAN_NOT_BE_NULL;
import static com.gym.crm.application.constant.Message.ID_MUST_BE_POSITIVE_NUMBER;
import static com.gym.crm.application.constant.Message.USER_FIELD_CAN_NOT_BE_EMPTY;
import static java.lang.String.format;

@Component
public class EntityValidator {

    public void checkUserValidation(User user) {
        Optional.ofNullable(user)
                .orElseThrow(() -> new ValidationException(format(ENTITY_CANNOT_BE_NULL_ERROR_TEMPLATE, "User")));

        Optional.ofNullable(user.getFirstName())
                .filter(name -> !name.isEmpty())
                .orElseThrow(() -> new ValidationException(format(USER_FIELD_CAN_NOT_BE_EMPTY, "firstName")));

        Optional.ofNullable(user.getLastName())
                .filter(name -> !name.isEmpty())
                .orElseThrow(() -> new ValidationException(format(USER_FIELD_CAN_NOT_BE_EMPTY, "lastName")));

        Optional.ofNullable(user.getUsername())
                .filter(name -> !name.isEmpty())
                .orElseThrow(() -> new ValidationException(format(USER_FIELD_CAN_NOT_BE_EMPTY, "username")));

        Optional.ofNullable(user.getPassword())
                .filter(name -> !name.isEmpty())
                .orElseThrow(() -> new ValidationException(format(USER_FIELD_CAN_NOT_BE_EMPTY, "password")));
    }

    public void checkTraineeValidation(Trainee trainee) {
        Optional.ofNullable(trainee)
                .orElseThrow(() -> new ValidationException(format(ENTITY_CANNOT_BE_NULL_ERROR_TEMPLATE, "Trainee")));
    }

    public void checkTrainerValidation(Trainer trainer) {
        Optional.ofNullable(trainer)
                .orElseThrow(() -> new ValidationException(format(ENTITY_CANNOT_BE_NULL_ERROR_TEMPLATE, "Trainer")));
    }

    public void checkTrainingValidation(Training training) {
        Optional.ofNullable(training)
                .orElseThrow(() -> new ValidationException(format(ENTITY_CANNOT_BE_NULL_ERROR_TEMPLATE, "Training")));
    }

    public void validateEntityId(Long id) {
        Optional.ofNullable(id)
                .orElseThrow(() -> new ValidationException(ID_CAN_NOT_BE_NULL));

        if (id <= 0) {
            throw new ValidationException(ID_MUST_BE_POSITIVE_NUMBER);
        }
    }
}
