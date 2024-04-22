package it.krisopea.springcors.service;

import it.krisopea.springcors.controller.model.AngularClientRequest;
import it.krisopea.springcors.exception.AppErrorCodeMessageEnum;
import it.krisopea.springcors.exception.AppException;
import it.krisopea.springcors.repository.AngularClientRepository;
import it.krisopea.springcors.repository.model.AngularClientEntity;
import it.krisopea.springcors.service.dto.AngularClientResponseDto;
import it.krisopea.springcors.service.mapper.MapperAngularClientDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AngularService {

    private final AngularClientRepository angularClientRepository;
    private final MapperAngularClientDto mapperAngularClientDto;

    public List<AngularClientResponseDto> getClients(){
        List<AngularClientEntity> usersEntity = angularClientRepository.findAll();
        if (usersEntity.isEmpty()){
            throw new AppException(AppErrorCodeMessageEnum.USER_NOT_EXISTS);
        }
        return mapperAngularClientDto.toClientResponseDto(usersEntity);
    }

    public void save(AngularClientRequest client) {
        AngularClientEntity angularUserEntity = mapperAngularClientDto.toAngularClientEntity(client);
        angularClientRepository.save(angularUserEntity);
    }
}
