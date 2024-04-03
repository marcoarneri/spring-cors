package it.krisopea.springcors.controller;

import it.krisopea.springcors.service.UserService;
import it.krisopea.springcors.service.mapper.MapperUserDto;
import it.krisopea.springcors.util.annotation.IsAdmin;
import it.krisopea.springcors.util.constant.PathConstants;
import it.krisopea.springcors.util.constant.PathMappingConstants;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathConstants.ADMIN_PATH)
@Slf4j
@Validated
@IsAdmin
public class AdminController {
  private final UserService userService;
  private final MapperUserDto mapperUserDto;

  @DeleteMapping("/delete/{" + PathMappingConstants.USER_ID + "}")
  public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
    log.info("Admin's delete request with user ID: {}", userId);

    userService.deleteUser(userId);

    log.info("Admin deleted user with user ID: {}", userId);
    return ResponseEntity.ok().build();
  }
}
