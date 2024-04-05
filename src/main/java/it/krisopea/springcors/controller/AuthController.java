package it.krisopea.springcors.controller;

import it.krisopea.springcors.controller.model.request.UserLoginRequest;
import it.krisopea.springcors.controller.model.request.UserRegistrationRequest;
import it.krisopea.springcors.service.AuthService;
import it.krisopea.springcors.service.dto.request.UserLoginRequestDto;
import it.krisopea.springcors.service.dto.request.UserRegistrationRequestDto;
import it.krisopea.springcors.service.mapper.MapperUserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
@Validated
public class AuthController {
  private final AuthService authService;
  private final MapperUserDto userMapperDto;

  @PostMapping("/login")
  public String loginUser(
      @ModelAttribute("userLoginRequest") @Valid UserLoginRequest userLoginRequest, Model model) {
    log.info("Login request for username: {}.", userLoginRequest.getUsername());

    UserLoginRequestDto userLoginRequestDto = userMapperDto.toUserLoginRequestDto(userLoginRequest);

    if (Boolean.FALSE.equals(authService.login(userLoginRequestDto))) {
      model.addAttribute("loginError", true);
      return "login";
    }

    log.info("Login completed successfully.");
    return "home";
  }

  @PostMapping("/register")
  public String registerUser(
      @ModelAttribute("userRegistrationRequest") @Valid UserRegistrationRequest request,
      Model model) {
    log.info(
        "Registration request for name: {}, surname: {}, email: {}, username: {}.",
        request.getName(),
        request.getSurname(),
        request.getEmail(),
        request.getUsername());

    UserRegistrationRequestDto requestDto = userMapperDto.toUserRegistrationRequestDto(request);

    if (Boolean.FALSE.equals(authService.register(requestDto))) {
      model.addAttribute("registerError", true);
      return "register";
    }

    log.info("Registration completed successfully.");
    return "home";
  }

  @GetMapping("/logout")
  public String logoutUser(Model model) {
    log.info(
        "Invalidating session for user: "
            + SecurityContextHolder.getContext().getAuthentication().getName());

    authService.logout();

    log.info("Logout completed successfully.");
    model.addAttribute("success", true);
    return "entry";
  }
}
