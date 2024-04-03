package it.krisopea.springcors.service.mapper;

import it.krisopea.springcors.controller.model.request.UserDeleteRequest;
import it.krisopea.springcors.controller.model.request.UserLoginRequest;
import it.krisopea.springcors.controller.model.request.UserRegistrationRequest;
import it.krisopea.springcors.controller.model.request.UserUpdateRequest;
import it.krisopea.springcors.service.dto.request.UserDeleteRequestDto;
import it.krisopea.springcors.service.dto.request.UserLoginRequestDto;
import it.krisopea.springcors.service.dto.request.UserRegistrationRequestDto;
import it.krisopea.springcors.service.dto.request.UserUpdateRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class MapperUserDto {
  public abstract UserLoginRequestDto toUserLoginRequestDto(UserLoginRequest userLoginRequest);

  public abstract UserRegistrationRequestDto toUserRegistrationRequestDto(
      UserRegistrationRequest userRegistrationRequest);

  public abstract UserUpdateRequestDto toUserUpdateDto(UserUpdateRequest userUpdateRequest);

  public abstract UserDeleteRequestDto toUserDeleteDto(UserDeleteRequest deleteUserRequest);
}
