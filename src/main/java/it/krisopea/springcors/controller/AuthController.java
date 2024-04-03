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
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@Slf4j
@Validated
@AllowAnonymous
public class AuthController {
  private final AuthService authService;
  private final MapperUserDto userMapperDto;

  @GetMapping("/login")
  public String loginUser(ModelMap model) {
    model.addAttribute("userLoginRequest", new UserLoginRequest());
    return "login";
  }

  @PostMapping("/perform_login")
  public String loginUser(@ModelAttribute("userLoginRequest") @Valid UserLoginRequest userLoginRequest) {
    log.info("Login request for username or email: {}.", userLoginRequest.getUsernameOrEmail());

    UserLoginRequestDto userLoginRequestDto = userMapperDto.toUserLoginRequestDto(userLoginRequest);
    authService.login(userLoginRequestDto);

    log.info("Login completed successfully.");
    return "home";
  }

  @GetMapping("/register")
  public String showRegistrationForm(ModelMap model) {
    model.addAttribute("userRegistrationRequest", new UserRegistrationRequest());
    return "register";
  }

  @PostMapping("/register")
  public String registerUser(@ModelAttribute("userRegistrationRequest") @Valid UserRegistrationRequest request) {
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

    return "home";
  }
}
