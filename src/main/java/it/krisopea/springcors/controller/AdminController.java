package it.krisopea.springcors.controller;

import it.krisopea.springcors.repository.UserRepository;
import it.krisopea.springcors.repository.model.UserEntity;
import it.krisopea.springcors.service.AdminService;
import it.krisopea.springcors.service.UserService;
import it.krisopea.springcors.util.constant.PathMappingConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminController {

  private final UserService userService;
  private final AdminService adminService;
  private final UserRepository userRepository;

  @DeleteMapping("/delete/{" + PathMappingConstants.USERNAME + "}")
  public ResponseEntity<Void> deleteUser(@PathVariable String username) {
    log.info("Admin's delete request with username: {}", username);

    userService.deleteUser(username);

    log.info("Admin deleted the user with username: {}", username);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/admin")
  public String adminPage(ModelMap model) {
    List<UserEntity> users = adminService.getUsersByRole();
    model.addAttribute("users", users);
    return "admin";
  }

  @GetMapping("/admin/update/{username}")
  public String getAdminUpdatePage(@PathVariable String username, ModelMap model) {
    UserEntity user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    model.addAttribute("userEntity", user);
    return "adminUpdate";
  }

  @PostMapping("/admin/update")
  public String adminUpdatePage(
      @ModelAttribute("userEntity") @Valid UserEntity user, ModelMap model) {
    log.info("Update request for username: {}, email: {}.", user.getUsername(), user.getEmail());

    adminService.updateUser(user);

    log.info("Admin updated the user with username: {}", user.getUsername());

    List<UserEntity> users = adminService.getUsersByRole();
    model.addAttribute("users", users);

    return "admin";
  }
}
