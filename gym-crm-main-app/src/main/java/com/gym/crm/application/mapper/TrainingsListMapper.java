package com.gym.crm.application.mapper;

import com.gym.crm.application.dto.criteria.TrainingsListCriteria;
import com.gym.crm.application.dto.request.TraineeTrainingsListRequest;
import com.gym.crm.application.dto.request.TrainerTrainingsListRequest;
import com.gym.crm.application.entity.Training;
import com.gym.crm.application.rest.dto.TraineeTrainingsListItem;
import com.gym.crm.application.rest.dto.TrainerTrainingsListItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TrainingsListMapper {

    TrainingsListMapper INSTANCE = Mappers.getMapper(TrainingsListMapper.class);

    @Mapping(source = "username", target = "traineeUsername")
    @Mapping(source = "fromDate", target = "fromDate")
    @Mapping(source = "toDate", target = "toDate")
    @Mapping(source = "trainerUsername", target = "trainerUsername")
    @Mapping(source = "trainingTypeName", target = "trainingTypeName")
    TrainingsListCriteria fromTraineeRequest(TraineeTrainingsListRequest req);

    @Mapping(source = "traineeUsername", target = "traineeUsername")
    @Mapping(source = "fromDate", target = "fromDate")
    @Mapping(source = "toDate", target = "toDate")
    @Mapping(source = "username", target = "trainerUsername")
    @Mapping(target = "trainingTypeName", ignore = true)
    TrainingsListCriteria fromTrainerRequest(TrainerTrainingsListRequest req);

    @Mapping(target = "trainername", source = "trainer.user.username")
    @Mapping(target = "trainingType", source = "trainingType.trainingTypeName", qualifiedByName = "toUpperCase")
    TraineeTrainingsListItem toTraineeTrainingsListItem(Training training);

    @Mapping(target = "traineename", source = "trainee.user.username")
    @Mapping(target = "trainingType", source = "trainingType.trainingTypeName", qualifiedByName = "toUpperCase")
    TrainerTrainingsListItem toTrainerTrainingsListItem(Training training);

    @Named("toUpperCase")
    default String toUpperCase(String value) {
        return value != null ? value.toUpperCase() : null;
    }

}
