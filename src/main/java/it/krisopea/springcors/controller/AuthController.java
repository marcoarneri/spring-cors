//package it.krisopea.springcors.controller;
//
//import it.krisopea.springcors.controller.model.UserLoginRequest;
//import it.krisopea.springcors.controller.model.UserRegistrationRequest;
//import it.krisopea.springcors.service.AuthService;
//import it.krisopea.springcors.service.dto.UserLoginRequestDto;
//import it.krisopea.springcors.service.mapper.MapperUserDto;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpSession;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//
//@Controller
//@RequiredArgsConstructor
//@Slf4j
//@Validated
//public class AuthController {
//  private final AuthService authService;
//  private final MapperUserDto userMapperDto;
//
//  @PostMapping("/login")
//  public void loginUser(
//      @ModelAttribute("userLoginRequest") @Valid UserLoginRequest userLoginRequest) {
//    log.info("Login request for username: {}.", userLoginRequest.getUsername());
//
//    UserLoginRequestDto userLoginRequestDto = userMapperDto.toUserLoginRequestDto(userLoginRequest);
//
//    authService.login(userLoginRequestDto);
//
//    log.info("Login completed successfully.");
//  }
//
//  @PostMapping("/register")
//  public String registerUser(
//      @ModelAttribute("userRegistrationRequest") @Valid
//          UserRegistrationRequest userRegistrationRequest,
//      Model model) {
//    log.info(
//        "Registration request for name: {}, surname: {}, email: {}, username: {}.",
//        userRegistrationRequest.getName(),
//        userRegistrationRequest.getSurname(),
//        userRegistrationRequest.getEmail(),
//        userRegistrationRequest.getUsername());
//
//    if (Boolean.FALSE.equals(
//        authService.register(
//            userMapperDto.toUserRegistrationRequestDto(userRegistrationRequest)))) {
//      model.addAttribute("registerError", true);
//      return "register";
//    }
//    log.info("Registration completed successfully.");
//    model.addAttribute("username", userRegistrationRequest.getUsername());
//    return "verification";
//  }
//
//  @GetMapping("/logout")
//  public void logout(HttpServletRequest request) {
//    HttpSession session = request.getSession(false);
//    if (session != null) {
//      session.invalidate();
//    }
//  }
//}
