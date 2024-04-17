package it.krisopea.springcors.controller;

import it.krisopea.springcors.controller.model.request.AdminUpdateRequest;
import it.krisopea.springcors.controller.model.request.RoleRequest;
import it.krisopea.springcors.repository.model.RoleEntity;
import it.krisopea.springcors.repository.model.UserEntity;
import it.krisopea.springcors.service.AdminService;
import it.krisopea.springcors.service.dto.request.AdminUpdateRequestDto;
import it.krisopea.springcors.service.mapper.MapperUserDto;
import it.krisopea.springcors.util.constant.PathConstants;
import it.krisopea.springcors.util.constant.PathMappingConstants;
import it.krisopea.springcors.util.constant.RoleConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(PathConstants.ADMIN_PATH)
public class AdminController {

  private final AdminService adminService;
  private final MapperUserDto mapperUserDto;

  @GetMapping()
  @PreAuthorize("hasAuthority('WRITE')")
  public String getAdminPage(ModelMap model) {
    List<UserEntity> users = adminService.getUsersByRole();
    model.addAttribute("users", users);
    return "admin";
  }

  /* -- ADMIN UPDATE -- */

  @GetMapping("/update/{" + PathMappingConstants.USERNAME + "}")
  @PreAuthorize("hasAuthority('WRITE')")
  public String getAdminUpdatePage(@PathVariable String username, ModelMap model) {
    Pair<List<RoleEntity>, AdminUpdateRequest> attributes =
        adminService.getAdminAttributes(username);

    model.addAttribute("roles", attributes.getLeft());
    model.addAttribute("adminUpdateRequest", attributes.getRight());
    return "admin-update";
  }

  @PostMapping("/update")
  @PreAuthorize("hasAuthority('WRITE')")
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

  /* -- ROLE PAGE-- */

  @GetMapping("/role")
  @PreAuthorize("hasAuthority('WRITE')")
  public String getRolePage(ModelMap model) {
    List<RoleEntity> roles = adminService.getRoles();
    model.addAttribute("roles", roles);
    return "role";
  }

  @GetMapping("/role/update/{" + PathMappingConstants.ROLE_NAME + "}")
  @PreAuthorize("hasAuthority('WRITE')")
  public String getRoleUpdatePage(@PathVariable String name, ModelMap model) {

    RoleRequest roleRequest = new RoleRequest();
    roleRequest.setName(name);
    model.addAttribute("roleRequest", roleRequest);
    model.addAttribute("privileges", adminService.getPrivileges());
    return "role-update";
  }

  @PostMapping("/role/update")
  @PreAuthorize("hasAuthority('WRITE')")
  public String roleUpdate(@ModelAttribute("roleRequest") RoleRequest roleRequest, ModelMap model) {
    adminService.updateRole(roleRequest);
    log.info("Admin updated the role with name: {}", roleRequest.getName());
    List<RoleEntity> roles = adminService.getRoles();
    model.addAttribute("roles", roles);
    return "role";
  }

  @GetMapping("/role/delete/{" + PathMappingConstants.ROLE_NAME + "}")
  @PreAuthorize("hasAuthority('DELETE')")
  public String deleteRole(@PathVariable String name, ModelMap model) {
    if (name.equals(RoleConstants.ROLE_USER)
            || name.equals(RoleConstants.ROLE_ADMIN)
            || name.equals(RoleConstants.ROLE_FOUNDER)) {
      List<RoleEntity> roles = adminService.getRoles();
      model.addAttribute("error", true);
      model.addAttribute("roles", roles);
      return "role";
    }
    adminService.deleteRole(name);
    log.info("Admin deleted the role with name: {}", name);
    List<RoleEntity> roles = adminService.getRoles();
    model.addAttribute("roles", roles);
    return "role";
  }

  /* -- NEW ROLE -- */

  @GetMapping("/new-role")
  @PreAuthorize("hasAuthority('WRITE')")
  public String getNewRolePage(ModelMap model) {
    model.addAttribute("role", new RoleRequest());
    model.addAttribute("privileges", adminService.getPrivileges());
    return "new-role";
  }

  @PostMapping("/new-role")
  @PreAuthorize("hasAuthority('WRITE')")
  public String addRole(@ModelAttribute("role") RoleRequest roleRequest, ModelMap model) {
    boolean role = adminService.createRole(roleRequest);
    if (!role){
      model.addAttribute("privileges", adminService.getPrivileges());
      model.addAttribute("error", true);
      return "new-role";
    }

    List<RoleEntity> roles = adminService.getRoles();
    model.addAttribute("roles", roles);

    return "role";
  }
}
