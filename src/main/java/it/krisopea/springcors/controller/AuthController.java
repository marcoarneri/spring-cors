package it.krisopea.springcors.controller;

import it.krisopea.springcors.controller.model.request.UserLoginRequest;
import it.krisopea.springcors.controller.model.request.UserRegistrationRequest;
import it.krisopea.springcors.service.AuthService;
import it.krisopea.springcors.service.dto.request.UserLoginRequestDto;
import it.krisopea.springcors.service.dto.request.UserRegistrationRequestDto;
import it.krisopea.springcors.service.mapper.MapperUserDto;
import it.krisopea.springcors.util.annotation.AllowAnonymous;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
@Slf4j
@Validated
@AllowAnonymous
public class AuthController {
  private final AuthService authService;
  private final MapperUserDto userMapperDto;

  @PostMapping("/register")
  public ResponseEntity<Void> registerUser(
      @Valid @RequestBody UserRegistrationRequest userRegistrationRequest) {
    log.info(
        "Registration request for name: {}, surname: {}, email: {}, username: {}.",
        userRegistrationRequest.getName(),
        userRegistrationRequest.getSurname(),
        userRegistrationRequest.getEmail(),
        userRegistrationRequest.getUsername());

    UserRegistrationRequestDto userRegistrationRequestDto =
        userMapperDto.toUserRegistrationRequestDto(userRegistrationRequest);
    authService.register(userRegistrationRequestDto);

    log.info("Registration completed successfully.");
    return ResponseEntity.ok().build();
  }

  @PostMapping("/login")
  public ResponseEntity<Void> loginUser(@Valid @RequestBody UserLoginRequest userLoginRequest) {
    log.info("Login request for username or email: {}.", userLoginRequest.getUsernameOrEmail());

    UserLoginRequestDto userLoginRequestDto = userMapperDto.toUserLoginRequestDto(userLoginRequest);
    authService.login(userLoginRequestDto);

    log.info("Login completed successfully.");
    return ResponseEntity.ok().build();
  }
}
