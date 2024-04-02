package it.krisopea.springcors.repository.mapper;

import it.krisopea.springcors.repository.model.DemoEntity;
import it.krisopea.springcors.service.dto.DemoRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class MapperDemoEntity {

    public abstract DemoEntity toEntity(DemoRequestDto requestDto);

}
