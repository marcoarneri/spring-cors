package it.krisopea.springcors.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.krisopea.springcors.controller.model.DemoRequest;
import it.krisopea.springcors.controller.model.DemoResponse;
import it.krisopea.springcors.service.DemoService;
import it.krisopea.springcors.service.dto.DemoRequestDto;
import it.krisopea.springcors.service.dto.DemoResponseDto;
import it.krisopea.springcors.service.mapper.MapperDemoDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final ProducerTemplate producerTemplate;

    @Operation(
            operationId = "demo",
            summary = "demo POST call",
            description = "example of POST rest api")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = DemoResponse.class))
                            })
            })
    @PostMapping(
            value = "/demo",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DemoResponse> demo(
            @Valid @RequestBody DemoRequest request) {
        log.info(
                "[demo] request body: [{}]",
                request);

        String routingEndpoint;

        if (request.getNoticeId().equals("valoreSpeciale")) {
            // Invia la richiesta al servizio speciale
            routingEndpoint = "direct:processDemoResponse";
            DemoResponseDto responseDto = producerTemplate.requestBody(routingEndpoint, request, DemoResponseDto.class);

            // Mappa la risposta e restituisci al client
            DemoResponse response = mapperDemoDto.toResponse(responseDto);
            return ResponseEntity.ok().body(response);
        }else {
            DemoRequestDto requestDto = mapperDemoDto.toRequestDto(request);

            DemoResponseDto responseDto = demoService.callDemoService(requestDto);

            DemoResponse response = mapperDemoDto.toResponse(responseDto);

            return ResponseEntity.ok().body(response);
        }
    }

}
