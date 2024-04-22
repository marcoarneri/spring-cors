//package it.krisopea.springcors.service;
//
//import it.krisopea.springcors.controller.model.AdminUpdateRequest;
//import it.krisopea.springcors.controller.model.RoleRequest;
//import it.krisopea.springcors.exception.AppErrorCodeMessageEnum;
//import it.krisopea.springcors.exception.AppException;
//import it.krisopea.springcors.repository.PrivilegeRepository;
//import it.krisopea.springcors.repository.RoleRepository;
//import it.krisopea.springcors.repository.UserRepository;
//import it.krisopea.springcors.repository.VerificationRepository;
//import it.krisopea.springcors.repository.mapper.MapperUserEntity;
//import it.krisopea.springcors.repository.model.PrivilegeEntity;
//import it.krisopea.springcors.repository.model.RoleEntity;
//import it.krisopea.springcors.repository.model.UserEntity;
//import it.krisopea.springcors.repository.model.VerificationEntity;
//import it.krisopea.springcors.service.dto.AdminUpdateRequestDto;
//import it.krisopea.springcors.util.constant.RoleConstants;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.tuple.Pair;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class AdminService {
//  private final UserRepository userRepository;
//  private final RoleRepository roleRepository;
//  private final PrivilegeRepository privilegeRepository;
//  private final VerificationRepository verificationRepository;
//  private final MapperUserEntity mapperUserEntity;
//
//  @PreAuthorize("hasAuthority('WRITE')")
//  public void updateUser(AdminUpdateRequestDto adminUpdateRequestDto) {
//    String username = adminUpdateRequestDto.getUsername();
//
//    UserEntity findUser =
//        userRepository
//            .findByUsername(username)
//            .orElseThrow(() -> new AppException(AppErrorCodeMessageEnum.BAD_REQUEST));
//
//    List<RoleEntity> roles = new ArrayList<>();
//
//    for (RoleEntity userRole : adminUpdateRequestDto.getRoles()) {
//      RoleEntity role = roleRepository.findByName(userRole.getName());
//      roles.add(role);
//    }
//    checkRoles(username, findUser, roles);
//    findUser.setRoles(roles);
//    userRepository.save(findUser);
//  }
//
//  @PreAuthorize("hasAuthority('WRITE')")
//  public void updateRole(RoleRequest roleRequest) {
//    RoleEntity findRole = roleRepository.findByName(roleRequest.getName());
//
//    findRole.setPrivileges(roleRequest.getPrivileges());
//    roleRepository.save(findRole);
//  }
//
//  @PreAuthorize("hasAuthority('DELETE')")
//  public void deleteUser(String username) {
//    UserEntity userEntity =
//        userRepository
//            .findByUsername(username)
//            .orElseThrow(() -> new AppException(AppErrorCodeMessageEnum.BAD_REQUEST));
//
//    Optional<VerificationEntity> verificationEntity =
//        verificationRepository.findByUsername(username);
//
//    verificationEntity.ifPresent(verificationRepository::delete);
//
//    userRepository.delete(userEntity);
//  }
//
//  @PreAuthorize("hasAuthority('DELETE')")
//  public void deleteRole(String name) {
//    List<UserEntity> findUsers = userRepository.findUsersByRoleName(name);
//    if (!findUsers.isEmpty()) {
//      for (UserEntity user : findUsers) {
//        List<RoleEntity> roles = new ArrayList<>();
//        RoleEntity role = roleRepository.findByName(RoleConstants.ROLE_VERIFIED);
//        roles.add(role);
//        user.setRoles(roles);
//        userRepository.save(user);
//      }
//    }
//    RoleEntity role = roleRepository.findByName(name);
//    roleRepository.deleteById(role.getId());
//  }
//
//  @PreAuthorize("hasAuthority('WRITE')")
//  public List<UserEntity> getUsersByRole() {
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    boolean isAdmin =
//        authentication.getAuthorities().stream()
//            .anyMatch(r -> r.getAuthority().equals(RoleConstants.ROLE_ADMIN));
//    if (isAdmin) {
//      return userRepository.findAllUsers();
//    } else {
//      return userRepository.findAllNotFounder();
//    }
//  }
//
//  @PreAuthorize("hasAuthority('WRITE')")
//  public Pair<List<RoleEntity>, AdminUpdateRequest> getAdminAttributes(String username) {
//    UserEntity user =
//        userRepository
//            .findByUsername(username)
//            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//    AdminUpdateRequest adminUpdateRequest = mapperUserEntity.toAdminUpdateRequest(user);
//
//    boolean isAdmin =
//        authentication.getAuthorities().stream()
//            .anyMatch(r -> r.getAuthority().equals(RoleConstants.ROLE_ADMIN));
//
//    List<RoleEntity> roles;
//    if (isAdmin) {
//      roles = roleRepository.findAllRolesNotFounder();
//    } else {
//      roles = roleRepository.findAll();
//    }
//    return Pair.of(roles, adminUpdateRequest);
//  }
//
//  private void checkRoles(String username, UserEntity entity, List<RoleEntity> roles) {
//    String roleName = roles.get(0).getName();
//    Optional<VerificationEntity> verificationEntity =
//        verificationRepository.findByUsername(username);
//
//    if (roleName.equals(RoleConstants.ROLE_VERIFIED)
//        || roleName.equals(RoleConstants.ROLE_ADMIN)
//        || roleName.equals(RoleConstants.ROLE_FOUNDER)) {
//      verificationEntity.ifPresent(verificationRepository::delete);
//    } else if (roleName.equals(RoleConstants.ROLE_USER) && (verificationEntity.isEmpty())) {
//      // FIXME
//      // authService.setupVerification(entity);
//    }
//  }
//
//  public List<PrivilegeEntity> getPrivileges() {
//    return privilegeRepository.findAll();
//  }
//
//  public List<RoleEntity> getRoles() {
//    return roleRepository.findAll();
//  }
//
//  public boolean createRole(RoleRequest roleRequest) {
//
//    String name = roleRequest.getName();
//    RoleEntity role = roleRepository.findByName(name);
//
//    if (role == null) {
//      role = new RoleEntity();
//      role.setName(name);
//      role.setPrivileges(roleRequest.getPrivileges());
//      roleRepository.save(role);
//      return true;
//    } else {
//      return false;
//    }
//  }
//}
