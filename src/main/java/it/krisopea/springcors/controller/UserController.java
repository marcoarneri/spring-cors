package it.krisopea.springcors.controller;

import it.krisopea.springcors.controller.model.request.UserDeleteRequest;
import it.krisopea.springcors.controller.model.request.UserLoginRequest;
import it.krisopea.springcors.controller.model.request.UserUpdateRequest;
import it.krisopea.springcors.service.AuthService;
import it.krisopea.springcors.service.UserService;
import it.krisopea.springcors.service.VerificationService;
import it.krisopea.springcors.service.dto.request.UserDeleteRequestDto;
import it.krisopea.springcors.service.dto.request.UserUpdateRequestDto;
import it.krisopea.springcors.service.mapper.MapperUserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
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
  private final VerificationService verificationService;
  private final MapperUserDto userMapperDto;
  private final MapperUserDto mapperUserDto;

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
    } else {
      log.warn("Verification failed.");
    }
    model.addAttribute("userLoginRequest", new UserLoginRequest());
    return "login";
  }

  @PostMapping("/sendVerification")
  public String sendVerificationEmail(@RequestParam("id") String id, ModelMap model) {
    String username = verificationService.getUsernameById(id);
    log.info("Another verification email attempt of: {}", username);

    Pair<Integer, Long> result = authService.sendRegistrationEmail(username);
    model.addAttribute("remainingAttempts", result.getLeft());
    model.addAttribute("id", id);
    model.addAttribute("delayUntilNextAttempt", result.getRight());
    return "redirect:/anon-page?id=" + id;
  }
}
