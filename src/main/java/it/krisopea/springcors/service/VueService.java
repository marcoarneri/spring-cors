package it.krisopea.springcors.service;

import it.krisopea.springcors.exception.AppErrorCodeMessageEnum;
import it.krisopea.springcors.exception.AppException;
import it.krisopea.springcors.repository.VueClientRepository;
import it.krisopea.springcors.repository.model.VueClientEntity;
import it.krisopea.springcors.service.dto.VueClientResponseDto;
import it.krisopea.springcors.service.mapper.MapperVueClientDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VueService {

    private final VueClientRepository vueClientRepository;
    private final MapperVueClientDto mapperVueClientDto;

    public List<VueClientResponseDto> getClients(){
        List<VueClientEntity> usersEntity = vueClientRepository.findAll();
        if (usersEntity.isEmpty()){
            throw new AppException(AppErrorCodeMessageEnum.USER_NOT_EXISTS);
        }
        return mapperVueClientDto.toClientResponseDto(usersEntity);
    }
}
