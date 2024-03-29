package it.krisopea.springcors.service.mapper;

import it.krisopea.springcors.controller.model.DemoRequest;
import it.krisopea.springcors.controller.model.DemoResponse;
import it.krisopea.springcors.service.dto.DemoRequestDto;
import it.krisopea.springcors.service.dto.DemoResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class MapperDemoDto {

    @Mapping(target = "location", expression = "java(mapLocation(request.getCity(), request.getNation()))")
    public abstract DemoRequestDto toRequestDto(DemoRequest request);

    public abstract DemoResponse toResponse(DemoResponseDto responseDto);

    public String mapLocation(String city, String nation){
        return nation + " - " + city;
    }
}
