package it.krisopea.springcors.controller;

import it.krisopea.springcors.controller.model.DemoRequest;
import it.krisopea.springcors.controller.model.DemoResponse;
import it.krisopea.springcors.service.DemoService;
import it.krisopea.springcors.service.dto.DemoRequestDto;
import it.krisopea.springcors.service.dto.DemoResponseDto;
import it.krisopea.springcors.service.mapper.MapperDemoDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
@Validated
public class DemoController {

    private final DemoService demoService;
    private final MapperDemoDto mapperDemoDto;

    @PostMapping(
            value = "/demo",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DemoResponse> demo(
            @Valid @RequestBody DemoRequest request) {

        log.info("demo request: [{}]", request.toString());

        DemoRequestDto requestDto = mapperDemoDto.toRequestDto(request);

        DemoResponseDto responseDto = demoService.callDemoService(requestDto);

        DemoResponse response = mapperDemoDto.toResponse(responseDto);

        return ResponseEntity.ok().body(response);
    }

}
