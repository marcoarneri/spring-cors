package it.krisopea.springcors.service;

import it.krisopea.springcors.controller.model.ReactClientRequest;
import it.krisopea.springcors.exception.AppErrorCodeMessageEnum;
import it.krisopea.springcors.exception.AppException;
import it.krisopea.springcors.repository.ReactClientRepository;
import it.krisopea.springcors.repository.model.ReactClientEntity;
import it.krisopea.springcors.service.dto.ReactClientResponseDto;
import it.krisopea.springcors.service.mapper.MapperAngularClientDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReactService {

    private final ReactClientRepository reactClientRepository;
    private final MapperAngularClientDto mapperAngularClientDto;

    public Pair<List<ReactClientResponseDto>, Integer> getClients(int page, int limit){
        Integer clientsSize = reactClientRepository.countAllClients();
        Pageable pageable = PageRequest.of(page, limit);
        Page<ReactClientEntity> usersEntity = reactClientRepository.findAll(pageable);
        if (usersEntity.isEmpty()){
            throw new AppException(AppErrorCodeMessageEnum.USER_NOT_EXISTS);
        }
        return Pair.of(mapperAngularClientDto.toClientResponseDto(usersEntity.getContent()), clientsSize);
    }

    public void save(ReactClientRequest client) {
        ReactClientEntity reactUserEntity = mapperAngularClientDto.toAngularClientEntity(client);
        reactUserEntity.setImgUrl("https://picsum.photos/400/200");
        ReactClientEntity entitySaved = reactClientRepository.save(reactUserEntity);
        log.info("Successfully saved cliente with id: {}", entitySaved.getId());
    }

    public void delete(Long id) {
        reactClientRepository.deleteById(id);
        log.info("Successfully deleted cliente with id: {}", id);
    }

    public ReactClientResponseDto getClient(Long id) {
        Optional<ReactClientEntity> usersEntity = reactClientRepository.findById(id);
        if (usersEntity.isEmpty()){
            throw new AppException(AppErrorCodeMessageEnum.USER_NOT_EXISTS);
        }
        return mapperAngularClientDto.toClientResponseDto(usersEntity.get());
    }
}
