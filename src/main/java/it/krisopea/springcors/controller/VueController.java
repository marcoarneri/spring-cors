package it.krisopea.springcors.controller;

import it.krisopea.springcors.controller.model.VueClientResponse;
import it.krisopea.springcors.controller.model.VueDataResponse;
import it.krisopea.springcors.service.VueService;
import it.krisopea.springcors.service.dto.VueClientResponseDto;
import it.krisopea.springcors.service.dto.VueDataResponseDto;
import it.krisopea.springcors.service.mapper.MapperVueClientDto;
import it.krisopea.springcors.service.mapper.MapperVueDataDto;
import it.krisopea.springcors.util.constant.PathConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(PathConstants.CLIENT_PATH)
@RequiredArgsConstructor
@Slf4j
@Validated
@CrossOrigin(origins = "*")
public class VueController {

    private final VueService vueService;
    private final MapperVueClientDto mapperVueClientDto;
    private final MapperVueDataDto mapperVueDataDto;

    @GetMapping(PathConstants.GET_CLIENTS)
    public ResponseEntity<List<VueClientResponse>> getClients() {
        List<VueClientResponseDto> usersDto = vueService.getClients();
        List<VueClientResponse> users = mapperVueClientDto.toClientResponse(usersDto);
        log.info("getClients sent [{}] items", users.size());
        return ResponseEntity.ok().body(users);
    }

    @GetMapping(PathConstants.GET_DATA)
    public ResponseEntity<List<VueDataResponse>> getData() {
        List<VueDataResponseDto> dataDto = vueService.getData();
        List<VueDataResponse> data = mapperVueDataDto.toClientResponse(dataDto);
        log.info("getData sent [{}] items", data.size());
        return ResponseEntity.ok().body(data);
    }
}
