package it.krisopea.springcors.controller;

import it.krisopea.springcors.repository.RoleRepository;
import it.krisopea.springcors.repository.UserRepository;
import it.krisopea.springcors.repository.model.RoleEntity;
import it.krisopea.springcors.repository.model.UserEntity;
import it.krisopea.springcors.service.AdminService;
import it.krisopea.springcors.service.UserService;
import it.krisopea.springcors.util.constant.PathConstants;
import it.krisopea.springcors.util.constant.PathMappingConstants;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(PathConstants.ADMIN_PATH)
public class AdminController {

  private final UserService userService;
  private final AdminService adminService;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  @DeleteMapping("/delete/{" + PathMappingConstants.USERNAME + "}")
  public ResponseEntity<Void> deleteUser(@PathVariable String username) {
    log.info("Admin's delete request with username: {}", username);

    userService.deleteUser(username);

    log.info("Admin deleted the user with username: {}", username);
    return ResponseEntity.ok().build();
  }

  @GetMapping()
  public String getAdminPage(ModelMap model) {
    List<UserEntity> users = adminService.getUsersByRole();
    model.addAttribute("users", users);
    return "admin";
  }

  @GetMapping("/update/{" + PathMappingConstants.USERNAME + "}")
  public String getAdminUpdatePage(@PathVariable String username, ModelMap model) {
    UserEntity user = userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(r -> r.getAuthority().equals("ADMIN"));
    List<RoleEntity> roles;
    if (isAdmin) {
        roles = roleRepository.findAllUserAndAdminRoles();
    } else {
        roles = roleRepository.findAll();
    }

    model.addAttribute("roles", roles);
    model.addAttribute("userEntity", user);

    return "admin-update";
  }

  @PostMapping("/update")
  public String getUpdatePage(
      @ModelAttribute("userEntity") @Valid UserEntity user, ModelMap model) {
    log.info("Update request for username: {}, email: {}.", user.getUsername(), user.getEmail());

    adminService.updateUser(user);

    log.info("Admin updated the user with username: {}", user.getUsername());

    List<UserEntity> users = adminService.getUsersByRole();
    model.addAttribute("users", users);

    return "admin";
  }
}
