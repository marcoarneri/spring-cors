package it.krisopea.springcors.service;

import it.krisopea.springcors.controller.model.request.AdminUpdateRequest;
import it.krisopea.springcors.exception.AppErrorCodeMessageEnum;
import it.krisopea.springcors.exception.AppException;
import it.krisopea.springcors.repository.RoleRepository;
import it.krisopea.springcors.repository.UserRepository;
import it.krisopea.springcors.repository.VerificationRepository;
import it.krisopea.springcors.repository.mapper.MapperUserEntity;
import it.krisopea.springcors.repository.model.RoleEntity;
import it.krisopea.springcors.repository.model.UserEntity;
import it.krisopea.springcors.repository.model.VerificationEntity;
import it.krisopea.springcors.service.dto.request.AdminUpdateRequestDto;
import it.krisopea.springcors.util.constant.RoleConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final VerificationRepository verificationRepository;
  private final MapperUserEntity mapperUserEntity;

  public void updateUser(AdminUpdateRequestDto adminUpdateRequestDto) {
    String username = adminUpdateRequestDto.getUsername();

    UserEntity findUser =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new AppException(AppErrorCodeMessageEnum.BAD_REQUEST));

    List<RoleEntity> roles = new ArrayList<>();
    for (RoleEntity userRole : adminUpdateRequestDto.getRoles()) {
      RoleEntity role = roleRepository.findByName(userRole.getName());
      roles.add(role);
    }

    findUser.setRoles(roles);
    userRepository.save(findUser);
  }

  public void deleteUser(String username) {
    UserEntity userEntity =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new AppException(AppErrorCodeMessageEnum.BAD_REQUEST));

    Optional<VerificationEntity> verificationEntity =
        verificationRepository.findByUserUsername(username);

    verificationEntity.ifPresent(verificationRepository::delete);

    userRepository.delete(userEntity);
  }

  public List<UserEntity> getUsersByRole() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    boolean isAdmin =
        authentication.getAuthorities().stream()
            .anyMatch(r -> r.getAuthority().equals(RoleConstants.ROLE_ADMIN));
    if (isAdmin) {
      return userRepository.findAllUsers();
    } else {
      return userRepository.findAllNotFounder();
    }
  }

  public Pair<List<RoleEntity>, AdminUpdateRequest> getAdminAttributes(String username) {
    UserEntity user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    AdminUpdateRequest adminUpdateRequest = mapperUserEntity.toAdminUpdateRequest(user);

    boolean isAdmin =
        authentication.getAuthorities().stream()
            .anyMatch(r -> r.getAuthority().equals(RoleConstants.ROLE_ADMIN));

    List<RoleEntity> roles;
    if (isAdmin) {
      roles = roleRepository.findAllUserAndAdminRoles();
    } else {
      roles = roleRepository.findAll();
    }
    return Pair.of(roles, adminUpdateRequest);
  }
}
