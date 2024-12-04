package com.gym.crm.application.mapper;

import com.gym.crm.application.entity.Trainee;
import com.gym.crm.application.entity.Trainer;
import com.gym.crm.application.rest.dto.TraineeItem;
import com.gym.crm.application.rest.dto.TrainerProfile;
import com.gym.crm.application.rest.dto.TrainerProfileResponse;
import com.gym.crm.application.rest.dto.TrainerUpdateProfileRequest;
import com.gym.crm.application.rest.dto.UpdateTrainerProfile200Response;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface TrainerMapper {

    TrainerMapper INSTANCE = Mappers.getMapper(TrainerMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trainees", ignore = true)
    @Mapping(target = "trainings", ignore = true)
    @Mapping(source = "firstName", target = "user.firstName")
    @Mapping(source = "lastName", target = "user.lastName")
    @Mapping(source = "specialization", target = "specialization.trainingTypeName")
    Trainer fromTrainerProfile(TrainerProfile dto);

    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "specialization", source = "specialization.trainingTypeName", qualifiedByName = "toUpperCase")
    @Mapping(target = "isActive", source = "user.active")
    @Mapping(target = "trainees", source = "trainees", qualifiedByName = "toTraineeItemList")
    TrainerProfileResponse toTrainerProfileResponse(Trainer trainer);

    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "username", source = "user.username")
    TraineeItem toTraineeItem(Trainee trainee);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trainees", ignore = true)
    @Mapping(target = "trainings", ignore = true)
    @Mapping(source = "firstName", target = "user.firstName")
    @Mapping(source = "lastName", target = "user.lastName")
    @Mapping(source = "specialization", target = "specialization.trainingTypeName", qualifiedByName = "toUpperCase")
    @Mapping(source = "isActive", target = "user.isActive")
    Trainer fromTrainerUpdateProfileRequest(TrainerUpdateProfileRequest dto);

    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "specialization", source = "specialization.trainingTypeName", qualifiedByName = "toUpperCase")
    @Mapping(target = "isActive", source = "user.active")
    @Mapping(target = "trainees", source = "trainees", qualifiedByName = "toTraineeItemList")
    UpdateTrainerProfile200Response toUpdateTrainerProfile200Response(Trainer trainer);

    @Named("toTraineeItemList")
    default List<TraineeItem> toTraineeItemList(Set<Trainee> trainees) {
        return trainees.stream()
                .map(this::toTraineeItem)
                .collect(Collectors.toList());
    }

    @Named("toUpperCase")
    default String toUpperCase(String value) {
        return value != null ? value.toUpperCase() : null;
    }

}
