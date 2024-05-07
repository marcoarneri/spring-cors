package it.krisopea.springcors.service.mapper;

import it.krisopea.springcors.controller.model.VueDataResponse;
import it.krisopea.springcors.repository.model.VueDataEntity;
import it.krisopea.springcors.service.dto.VueDataResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class MapperVueDataDto {

    public abstract List<VueDataResponseDto> toClientResponseDto(List<VueDataEntity> dataEntity);

    public abstract VueDataResponseDto toClientResponseDto(VueDataEntity dataEntity);

    public abstract List<VueDataResponse> toClientResponse(List<VueDataResponseDto> dataResponseDto);

    public abstract VueDataResponse toClientResponse(VueDataResponseDto dataResponseDto);
}
