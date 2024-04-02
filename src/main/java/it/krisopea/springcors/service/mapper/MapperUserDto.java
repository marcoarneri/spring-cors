package it.krisopea.springcors.service.mapper;

import it.krisopea.springcors.controller.model.request.UserLoginRequest;
import it.krisopea.springcors.controller.model.response.UserResponse;
import it.krisopea.springcors.service.dto.request.UserLoginRequestDto;
import it.krisopea.springcors.service.dto.response.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class MapperUserDto {

  public abstract UserLoginRequestDto toRequestDto(UserLoginRequest request);

  public abstract UserResponse toResponse(UserResponseDto responseDto);

  public String mapLocation(String city, String nation) {
    return nation + " - " + city;
  }
}
