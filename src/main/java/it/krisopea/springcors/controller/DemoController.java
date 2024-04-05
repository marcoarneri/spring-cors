package it.krisopea.springcors.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.krisopea.springcors.controller.model.DemoRequest;
import it.krisopea.springcors.controller.model.DemoResponse;
import it.krisopea.springcors.service.DemoService;
import it.krisopea.springcors.service.KafkaReplyService;
import it.krisopea.springcors.service.dto.DemoRequestDto;
import it.krisopea.springcors.service.dto.DemoResponseDto;
import it.krisopea.springcors.service.mapper.MapperDemoDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyMessageFuture;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
@Validated
public class DemoController {

    private final DemoService demoService;
    private final KafkaReplyService kafkaReplyService;
    private final MapperDemoDto mapperDemoDto;

    @PostMapping(
            value = "/demo",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DemoResponse> demo(
            @Valid @RequestBody DemoRequest request) throws JsonProcessingException {

        log.info("demo request: [{}]", request.toString());

        DemoRequestDto requestDto = mapperDemoDto.toRequestDto(request);

        DemoResponseDto responseDto = demoService.callDemoService(requestDto);

        DemoResponse response = mapperDemoDto.toResponse(responseDto);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping(
            value = "/demoReply/{correlation}/{data}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> demoReply(@PathVariable String correlation, @PathVariable String data) throws Exception {

        String message = correlation +", "+ data;
        Object messageReceived = kafkaReplyService.kafkaRequestReply(message);

        return ResponseEntity.ok().body(messageReceived);
    }

}
