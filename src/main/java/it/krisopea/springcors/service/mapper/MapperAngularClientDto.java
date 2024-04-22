package it.krisopea.springcors.service.mapper;

import it.krisopea.springcors.controller.model.AngularClientRequest;
import it.krisopea.springcors.controller.model.AngularClientResponse;
import it.krisopea.springcors.repository.model.AngularClientEntity;
import it.krisopea.springcors.service.dto.AngularClientResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class MapperAngularClientDto {

    public abstract List<AngularClientResponseDto> toClientResponseDto(List<AngularClientEntity> usersEntity);

    public abstract AngularClientResponseDto toClientResponseDto(AngularClientEntity userEntity);

    public abstract List<AngularClientResponse> toClientResponse(List<AngularClientResponseDto> usersResponseDto);

    public abstract AngularClientResponse toClientResponse(AngularClientResponseDto userResponseDto);

    public abstract AngularClientEntity toAngularClientEntity(AngularClientRequest user);
}
