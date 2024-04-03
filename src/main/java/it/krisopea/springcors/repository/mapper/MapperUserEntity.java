package it.krisopea.springcors.repository.mapper;

import it.krisopea.springcors.repository.model.UserEntity;
import it.krisopea.springcors.service.dto.request.UserLoginRequestDto;
import it.krisopea.springcors.service.dto.request.UserRegistrationRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class MapperUserEntity {

  public abstract UserEntity toUserEntity(UserLoginRequestDto requestDto);

  public abstract UserEntity toUserEntity(UserRegistrationRequestDto requestDto);
}
