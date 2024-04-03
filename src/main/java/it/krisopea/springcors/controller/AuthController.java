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
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
@Slf4j
@Validated
@AllowAnonymous
public class AuthController {
  private final AuthService authService;
  private final MapperUserDto userMapperDto;

  //TODO adattarlo alle view
  @PostMapping("/login")
  public ResponseEntity<Void> loginUser(@Valid @RequestBody UserLoginRequest userLoginRequest) {
    log.info("Login request for username or email: {}.", userLoginRequest.getUsernameOrEmail());

    UserLoginRequestDto userLoginRequestDto = userMapperDto.toUserLoginRequestDto(userLoginRequest);
    authService.login(userLoginRequestDto);

    log.info("Login completed successfully.");
    return ResponseEntity.ok().build();
  }

  @GetMapping("/register")
  public ModelAndView showRegistrationForm(ModelMap model) {
    model.addAttribute("userRegistrationRequest", new UserRegistrationRequest());
    return new ModelAndView("register", model);
  }

  @PostMapping("/register")
  public ModelAndView registerUser(@ModelAttribute("userRegistrationRequest") @Valid UserRegistrationRequest request, ModelMap model) {
    log.info(
            "Registration request for name: {}, surname: {}, email: {}, username: {}.",
            request.getName(),
            request.getSurname(),
            request.getEmail(),
            request.getUsername());

    UserRegistrationRequestDto requestDto =
            userMapperDto.toUserRegistrationRequestDto(request);
    authService.register(requestDto);

    log.info("Registration completed successfully.");

    return new ModelAndView("login");
  }
}
