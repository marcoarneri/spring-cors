package it.krisopea.springcors.service.mapper;

import it.krisopea.springcors.controller.model.*;
import it.krisopea.springcors.service.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class MapperUserDto {
  public abstract UserLoginRequestDto toUserLoginRequestDto(UserLoginRequest userLoginRequest);

  public abstract UserRegistrationRequestDto toUserRegistrationRequestDto(
      UserRegistrationRequest userRegistrationRequest);

  public abstract UserUpdateRequestDto toUserUpdateDto(UserUpdateRequest userUpdateRequest);

  public abstract UserDeleteRequestDto toUserDeleteDto(UserDeleteRequest deleteUserRequest);

  public abstract AdminUpdateRequestDto toAdminUpdateRequestDto(
      AdminUpdateRequest adminUpdateRequest);
}
