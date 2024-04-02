package it.krisopea.springcors.service.mapper;

import it.krisopea.springcors.controller.model.DemoResponse;
import it.krisopea.springcors.controller.model.UserLoginRequest;
import it.krisopea.springcors.service.dto.DemoResponseDto;
import it.krisopea.springcors.service.dto.UserLoginRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class MapperDemoDto {

  @Mapping(
      target = "location",
      expression = "java(mapLocation(request.getCity(), request.getNation()))")
  public abstract UserLoginRequestDto toRequestDto(UserLoginRequest request);

  public abstract DemoResponse toResponse(DemoResponseDto responseDto);

  public String mapLocation(String city, String nation) {
    return nation + " - " + city;
  }
}
