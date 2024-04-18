package it.krisopea.springcors.controller;

import it.krisopea.springcors.controller.model.request.UserDeleteRequest;
import it.krisopea.springcors.controller.model.request.UserLoginRequest;
import it.krisopea.springcors.controller.model.request.UserUpdateRequest;
import it.krisopea.springcors.repository.model.UserEntity;
import it.krisopea.springcors.service.AuthService;
import it.krisopea.springcors.service.UserService;
import it.krisopea.springcors.service.dto.request.UserDeleteRequestDto;
import it.krisopea.springcors.service.dto.request.UserUpdateRequestDto;
import it.krisopea.springcors.service.mapper.MapperUserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
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
  @PreAuthorize("hasAuthority('DELETE')")
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

  @GetMapping("/linkVerify")
  public String linkVerify(
          @RequestParam(name = "token") String token,
          ModelMap model) {

    log.info("Verifying user with token: {}", token);

    if (userService.verifyUser(token).equals(Boolean.TRUE)) {
      log.info("User verified successfully.");
    } else {
      log.warn("Verification failed.");
    }
    model.addAttribute("userLoginRequest", new UserLoginRequest());
    model.addAttribute("verified", true);
    return "login";
  }

  @PostMapping("/sendVerification")
  public String sendVerificationEmail(@RequestParam("username") String username, @RequestParam("email") String email, ModelMap model) {
    log.info("Sending another verification email to: {}", username);

    boolean flag = authService.resendEmail(username, email);

    if (!flag) {
      model.addAttribute("already", true);
      return "login";
    }
    model.addAttribute("username", username);
    model.addAttribute("success", true);
    return "verification";
  }

  @PostMapping("/sendChangePassword")
  public String sendChangePassword(@RequestParam("email") String email, ModelMap model) {
    log.info("Sending email to change password: {}", email);

    userService.sendEmailToChangePassword(email);

    model.addAttribute("success", true);
    return "forgotPassword";
  }

  @GetMapping("/changePassword")
  public String getChangePassword(@RequestParam("id") String id, ModelMap model) {
    log.info("change password with id: {}", id);

    UserEntity userEntity = userService.verifyUserId(id);
    model.addAttribute("username", userEntity.getUsername());

    return "changePassword";
  }

  @GetMapping("/forgotPassword")
  public String forgotPassword(ModelMap model) {
    log.info("redirect to forgot password page");

    return "forgotPassword";
  }

  @PostMapping("/updatePassword")
  public String updatePassword(@RequestParam("password1") String password1, @RequestParam("password2") String password2, @RequestParam("username") String username, ModelMap model) {

    boolean passwordMatch = userService.verifyPasswordMatch(password1, password2);
    if (!passwordMatch){
      model.addAttribute("username", username);
      model.addAttribute("passwordError", true);
      return "changePassword";
    }

    Pair<Boolean, UserEntity> pairUserEntity = userService.isPasswordOld(password1, username);
    if (!pairUserEntity.getLeft()){
      model.addAttribute("username", username);
      model.addAttribute("isPasswordOld", true);
      return "changePassword";
    }

    userService.updatePassword(password1, pairUserEntity.getRight());

    model.addAttribute("successChangePassword", true);
    model.addAttribute("userLoginRequest", new UserLoginRequest());
    return "login";
  }
}
