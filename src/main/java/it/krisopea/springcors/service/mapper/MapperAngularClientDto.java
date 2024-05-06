package it.krisopea.springcors.service.mapper;

import it.krisopea.springcors.controller.model.ReactClientRequest;
import it.krisopea.springcors.controller.model.ReactClientResponse;
import it.krisopea.springcors.repository.model.ReactClientEntity;
import it.krisopea.springcors.service.dto.ReactClientResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class MapperAngularClientDto {

    public abstract List<ReactClientResponseDto> toClientResponseDto(List<ReactClientEntity> usersEntity);

    public abstract ReactClientResponseDto toClientResponseDto(ReactClientEntity userEntity);

    public abstract List<ReactClientResponse> toClientResponse(List<ReactClientResponseDto> usersResponseDto);

    public abstract ReactClientResponse toClientResponse(ReactClientResponseDto userResponseDto);

    public abstract ReactClientEntity toAngularClientEntity(ReactClientRequest user);
}
