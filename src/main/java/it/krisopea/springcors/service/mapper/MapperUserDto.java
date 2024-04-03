package it.krisopea.springcors.service.mapper;

import it.krisopea.springcors.controller.model.request.UserLoginRequest;
import it.krisopea.springcors.controller.model.request.UserRegistrationRequest;
import it.krisopea.springcors.controller.model.response.UserResponse;
import it.krisopea.springcors.service.dto.request.UserLoginRequestDto;
import it.krisopea.springcors.service.dto.request.UserRegistrationRequestDto;
import it.krisopea.springcors.service.dto.response.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class MapperUserDto {
  public abstract UserLoginRequestDto toUserLoginRequestDto(UserLoginRequest userLoginRequest);

  public abstract UserRegistrationRequestDto toUserRegistrationRequestDto(
      UserRegistrationRequest userRegistrationRequest);

  public abstract UserResponse toUserResponse(UserResponseDto responseDto);
}
