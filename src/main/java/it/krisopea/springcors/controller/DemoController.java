package it.krisopea.springcors.controller;

import it.krisopea.springcors.controller.model.DemoRequest;
import it.krisopea.springcors.controller.model.DemoResponse;
import it.krisopea.springcors.service.DemoService;
import it.krisopea.springcors.service.dto.DemoRequestDto;
import it.krisopea.springcors.service.dto.DemoResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@Validated
public class DemoController {

    private final DemoService demoService;

    @PostMapping(
            value = "/demo",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DemoResponse> demo(
            @Valid @RequestBody DemoRequest request) {

        DemoRequestDto requestDto = new DemoRequestDto();
        requestDto.setIuv(request.getIuv());
        requestDto.setNoticeId(request.getNoticeId());
        requestDto.setLocation(request.getNation() + " - " + request.getCity());

        DemoResponseDto responseDto = demoService.callDemoService(requestDto);

        DemoResponse response = new DemoResponse();
        response.setOutcome(responseDto.getOutcome());
        response.setStatus(responseDto.getStatus());

        return ResponseEntity.ok().body(response);
    }

}
