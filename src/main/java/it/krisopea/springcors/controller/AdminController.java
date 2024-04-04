package it.krisopea.springcors.controller;

import it.krisopea.springcors.service.UserService;
import it.krisopea.springcors.util.annotation.IsAdmin;
import it.krisopea.springcors.util.constant.PathConstants;
import it.krisopea.springcors.util.constant.PathMappingConstants;
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

  @DeleteMapping("/delete/{" + PathMappingConstants.USERNAME + "}")
  public ResponseEntity<Void> deleteUser(@PathVariable String username) {
    log.info("Admin's delete request with username: {}", username);

    userService.deleteUser(username);

    log.info("Admin deleted the user with username: {}", username);
    return ResponseEntity.ok().build();
  }
}
