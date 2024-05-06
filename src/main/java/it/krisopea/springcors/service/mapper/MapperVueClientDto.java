package it.krisopea.springcors.service.mapper;

import it.krisopea.springcors.controller.model.VueClientResponse;
import it.krisopea.springcors.repository.model.VueClientEntity;
import it.krisopea.springcors.service.dto.VueClientResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class MapperVueClientDto {

    public abstract List<VueClientResponseDto> toClientResponseDto(List<VueClientEntity> usersEntity);

    public abstract VueClientResponseDto toClientResponseDto(VueClientEntity userEntity);

    public abstract List<VueClientResponse> toClientResponse(List<VueClientResponseDto> usersResponseDto);

    public abstract VueClientResponse toClientResponse(VueClientResponseDto userResponseDto);
}
