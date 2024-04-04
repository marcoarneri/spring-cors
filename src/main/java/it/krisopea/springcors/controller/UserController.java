package it.krisopea.springcors.controller;

import it.krisopea.springcors.controller.model.request.UserDeleteRequest;
import it.krisopea.springcors.controller.model.request.UserUpdateRequest;
import it.krisopea.springcors.service.UserService;
import it.krisopea.springcors.service.dto.request.UserDeleteRequestDto;
import it.krisopea.springcors.service.dto.request.UserUpdateRequestDto;
import it.krisopea.springcors.service.mapper.MapperUserDto;
import it.krisopea.springcors.util.annotation.IsAuthenticated;
import it.krisopea.springcors.util.constant.PathConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathConstants.USER_PATH)
@Slf4j
@Validated
@IsAuthenticated
public class UserController {
  private final UserService userService;
  private final MapperUserDto mapperUserDto;

  @PutMapping("/update")
  public ResponseEntity<Void> updateUser(@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    log.info("Update request for user with username: {}", username);

    UserUpdateRequestDto userUpdateRequestDto = mapperUserDto.toUserUpdateDto(userUpdateRequest);
    userService.updateUser(userUpdateRequestDto, username);

    log.info("Updated user with username: {}", username);
    return ResponseEntity.ok().build();
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
