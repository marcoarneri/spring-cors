package it.krisopea.springcors.controller;

import it.krisopea.springcors.controller.model.request.AdminUpdateRequest;
import it.krisopea.springcors.repository.model.RoleEntity;
import it.krisopea.springcors.repository.model.UserEntity;
import it.krisopea.springcors.service.AdminService;
import it.krisopea.springcors.service.dto.request.AdminUpdateRequestDto;
import it.krisopea.springcors.service.mapper.MapperUserDto;
import it.krisopea.springcors.util.constant.PathConstants;
import it.krisopea.springcors.util.constant.PathMappingConstants;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.access.prepost.PreAuthorize;
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

  private final AdminService adminService;
  private final MapperUserDto mapperUserDto;

  @GetMapping()
  public String getAdminPage(ModelMap model) {
    List<UserEntity> users = adminService.getUsersByRole();
    model.addAttribute("users", users);
    return "admin";
  }

  /* -- ADMIN UPDATE -- */

  @GetMapping("/update/{" + PathMappingConstants.USERNAME + "}")
  public String getAdminUpdatePage(@PathVariable String username, ModelMap model) {
    Pair<List<RoleEntity>, AdminUpdateRequest> attributes =
        adminService.getAdminAttributes(username);

    model.addAttribute("roles", attributes.getLeft());
    model.addAttribute("adminUpdateRequest", attributes.getRight());
    return "admin-update";
  }

  @PostMapping("/update")
  public String updateUser(
      @ModelAttribute("adminUpdateRequest") @Valid AdminUpdateRequest adminUpdateRequest,
      ModelMap model) {
    log.info(
        "Update request for username: {}, email: {}.",
        adminUpdateRequest.getUsername(),
        adminUpdateRequest.getEmail());

    AdminUpdateRequestDto adminUpdateRequestDto =
        mapperUserDto.toAdminUpdateRequestDto(adminUpdateRequest);
    adminService.updateUser(adminUpdateRequestDto);

    log.info("Admin updated the user with username: {}", adminUpdateRequestDto.getUsername());

    List<UserEntity> users = adminService.getUsersByRole();
    model.addAttribute("users", users);

    return "admin";
  }

  /* -- ADMIN DELETE -- */

  @GetMapping("/delete/{" + PathMappingConstants.USERNAME + "}")
  @PreAuthorize("hasAuthority('DELETE')")
  public String deleteUser(@PathVariable String username, ModelMap model) {
    adminService.deleteUser(username);
    log.info("Admin deleted the user with username: {}", username);

    List<UserEntity> users = adminService.getUsersByRole();
    model.addAttribute("users", users);

    return "admin";
  }
}
