package com.gym.crm.application.mapper;

import com.gym.crm.application.entity.TrainingType;
import com.gym.crm.application.rest.dto.TrainingTypeListResponseInner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TrainingTypeMapper {

    TrainingTypeMapper INSTANCE = Mappers.getMapper(TrainingTypeMapper.class);

    @Mapping(target = "type", source = "trainingTypeName")
    @Mapping(target = "typeId", source = "id")
    TrainingTypeListResponseInner from(TrainingType trainingType);

}
