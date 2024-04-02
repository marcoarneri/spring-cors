package it.krisopea.springcors.controller;

import it.krisopea.springcors.controller.model.request.UserLoginRequest;
import it.krisopea.springcors.controller.model.response.DemoResponse;
import it.krisopea.springcors.service.UserService;
import it.krisopea.springcors.service.dto.DemoResponseDto;
import it.krisopea.springcors.service.dto.request.UserLoginRequestDto;
import it.krisopea.springcors.service.mapper.MapperUserDto;
import it.krisopea.springcors.util.annotation.AnyRole;
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
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
@Validated
@AnyRole
public class UserController {

  private final UserService userService;
  private final MapperUserDto mapperUserDto;

  @PostMapping(
      value = "/demo",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<DemoResponse> demo(@Valid @RequestBody UserLoginRequest request) {

    log.info("demo request: [{}]", request.toString());

    UserLoginRequestDto requestDto = mapperUserDto.toRequestDto(request);

    DemoResponseDto responseDto = userService.callDemoService(requestDto);

    DemoResponse response = mapperUserDto.toResponse(responseDto);

    return ResponseEntity.ok().body(response);
  }
}
