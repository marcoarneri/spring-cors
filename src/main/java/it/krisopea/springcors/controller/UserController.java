package it.krisopea.springcors.controller;

import it.krisopea.springcors.controller.model.request.UserDeleteRequest;
import it.krisopea.springcors.controller.model.request.UserLoginRequest;
import it.krisopea.springcors.controller.model.request.UserUpdateRequest;
import it.krisopea.springcors.service.AuthService;
import it.krisopea.springcors.service.UserService;
import it.krisopea.springcors.service.dto.request.UserDeleteRequestDto;
import it.krisopea.springcors.service.dto.request.UserUpdateRequestDto;
import it.krisopea.springcors.service.mapper.MapperUserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
  private final UserService userService;
  private final AuthService authService;
  private final MapperUserDto userMapperDto;
  private final MapperUserDto mapperUserDto;

  @Value("${verification.maxAttempts}")
  private int maxAttempts;

  @PostMapping("/update")
  @PreAuthorize("hasAuthority('UPDATE')")
  public String updateUser(
      @ModelAttribute("userUpdateRequest") @Valid UserUpdateRequest request, Model model) {
    log.info(
        "Update request for name: {}, surname: {}, email: {}, username: {}.",
        request.getName(),
        request.getSurname(),
        request.getEmail(),
        request.getUsername());

    UserUpdateRequestDto requestDto = userMapperDto.toUserUpdateDto(request);

    if (Boolean.TRUE.equals(userService.updateUser(requestDto))) {
      model.addAttribute("userLoginRequest", new UserLoginRequest());
      model.addAttribute("updateUser", true);
      SecurityContextHolder.clearContext();
      return "login";
    }

    log.info("Update completed successfully.");
    model.addAttribute(
        "username", SecurityContextHolder.getContext().getAuthentication().getName());
    return "home";
  }

  @DeleteMapping("/delete")
  public ResponseEntity<Void> deleteUser(@Valid @RequestBody UserDeleteRequest deleteUserRequest) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    log.info("Delete request with username: {}", username);

    UserDeleteRequestDto userDeleteRequestDto = mapperUserDto.toUserDeleteDto(deleteUserRequest);
    userService.deleteUser(userDeleteRequestDto, username);

    log.info("Deleted user with username: {}", username);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/verify")
  public String verifyUser(@RequestParam("token") String tokenString, ModelMap model) {
    log.info("Verifying user with token: {}", tokenString);

    if (userService.verifyUser(tokenString).equals(Boolean.TRUE)) {
      log.info("User verified successfully.");
      model.addAttribute("success", true);
    } else {
      log.warn("Verification failed.");
      model.addAttribute("error", true);
    }
    return "home";
  }

  @PostMapping("/sendVerification")
  public String sendVerificationEmail() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    log.info("Sending another verification email to: {}", username);
    //    Integer remainingAttempts = authService.sendVerificationEmail(username);
    // TODO mettere in caso di errore modelmap con cooldown
    SecurityContextHolder.clearContext();
    return "home";
  }
}
