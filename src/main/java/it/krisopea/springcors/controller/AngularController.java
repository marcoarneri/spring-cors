package it.krisopea.springcors.controller;

import it.krisopea.springcors.controller.model.AngularClientRequest;
import it.krisopea.springcors.controller.model.AngularClientResponse;
import it.krisopea.springcors.service.AngularService;
import it.krisopea.springcors.service.dto.AngularClientResponseDto;
import it.krisopea.springcors.service.mapper.MapperAngularClientDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
@Slf4j
@Validated
@CrossOrigin(origins = "http://localhost:4200")
public class AngularController {

    private final AngularService angularService;
    private final MapperAngularClientDto mapperAngularClientDto;

    @GetMapping("/get")
    public List<AngularClientResponse> getClients() {
        List<AngularClientResponseDto> usersDto = angularService.getClients();
        List<AngularClientResponse> users = mapperAngularClientDto.toClientResponse(usersDto);
        return users;
    }

    @PostMapping("/add")
    void addClient(@RequestBody AngularClientRequest request) {
        angularService.save(request);
    }

}
