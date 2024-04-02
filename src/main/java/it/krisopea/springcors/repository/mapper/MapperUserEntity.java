package it.krisopea.springcors.repository.mapper;

import it.krisopea.springcors.repository.model.DemoUserEntity;
import it.krisopea.springcors.service.dto.UserLoginRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class MapperUserEntity {

    public abstract DemoUserEntity toEntity(UserLoginRequestDto requestDto);

}
