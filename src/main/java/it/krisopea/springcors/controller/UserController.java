package it.krisopea.springcors.controller;

import it.krisopea.springcors.controller.model.request.UserDeleteRequest;
import it.krisopea.springcors.controller.model.request.UserLoginRequest;
import it.krisopea.springcors.controller.model.request.UserUpdateRequest;
import it.krisopea.springcors.service.UserService;
import it.krisopea.springcors.service.dto.request.UserDeleteRequestDto;
import it.krisopea.springcors.service.dto.request.UserUpdateRequestDto;
import it.krisopea.springcors.service.mapper.MapperUserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

  private final UserService userService;
  private final MapperUserDto userMapperDto;
  private final MapperUserDto mapperUserDto;

  @PostMapping("/update")
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
      return "redirect:/logout";
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
}
