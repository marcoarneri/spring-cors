package it.krisopea.springcors.service;

import it.krisopea.springcors.exception.AppErrorCodeMessageEnum;
import it.krisopea.springcors.exception.AppException;
import it.krisopea.springcors.repository.UserRepository;
import it.krisopea.springcors.repository.model.UserEntity;
import it.krisopea.springcors.service.dto.request.UserDeleteRequestDto;
import it.krisopea.springcors.service.dto.request.UserUpdateRequestDto;
import it.krisopea.springcors.util.annotation.IsAdmin;
import it.krisopea.springcors.util.constant.RoleConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.apache.logging.log4j.util.Strings.isBlank;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired private ProducerTemplate producerTemplate;

  @PreAuthorize(
      "@authenticatedUserUtils.hasId(#userId) and hasAnyRole('"
          + RoleConstants.ROLE_USER
          + "', '"
          + RoleConstants.ROLE_ADMIN
          + "')")
  public void updateUser(UserUpdateRequestDto userUpdateRequestDto, UUID userId) {
    UserEntity userEntity =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new AppException(AppErrorCodeMessageEnum.BAD_REQUEST));

    String name = userUpdateRequestDto.getName();
    String surname = userUpdateRequestDto.getSurname();
    String password = userUpdateRequestDto.getPassword();
    String oldEncodedPassword = passwordEncoder.encode(userUpdateRequestDto.getOldPassword());

    if (oldEncodedPassword.equals(userEntity.getPassword())) {
      throw new AppException(AppErrorCodeMessageEnum.BAD_REQUEST);
    }

    if (isBlank(name) && isBlank(surname) && isBlank(password)) {
      throw new AppException(AppErrorCodeMessageEnum.BAD_REQUEST);
    }

    if (isNotBlank(name)) {
      userEntity.setName(name);
    }
    if (isNotBlank(surname)) {
      userEntity.setSurname(surname);
    }
    if (isNotBlank(password)) {
      userEntity.setPassword(passwordEncoder.encode(password));
    }

    userEntity.setUsername(null);
    userEntity.setEmail(null);
    userRepository.saveAndFlush(userEntity);
    producerTemplate.sendBodyAndHeader(
        "direct:sendUpdateEmail", null, "email", userEntity.getEmail());
  }

  @PreAuthorize(
      "@authenticatedUserUtils.hasId(#userId) and hasAnyRole('"
          + RoleConstants.ROLE_USER
          + "', '"
          + RoleConstants.ROLE_ADMIN
          + "')")
  public void deleteUser(UserDeleteRequestDto userDeleteRequestDto, UUID userId) {
    UserEntity userEntity =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new AppException(AppErrorCodeMessageEnum.BAD_REQUEST));

    String encryptedPassword = passwordEncoder.encode(userDeleteRequestDto.getPassword());

    if (!encryptedPassword.equals(userEntity.getPassword())) {
      throw new AppException(AppErrorCodeMessageEnum.BAD_REQUEST);
    } else {
      userRepository.delete(userEntity);
      producerTemplate.sendBodyAndHeader(
          "direct:sendDeleteEmail", null, "email", userDeleteRequestDto.getEmail());
    }
  }

  @IsAdmin
  public void deleteUser(UUID userId) {
    UserEntity userEntity =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new AppException(AppErrorCodeMessageEnum.BAD_REQUEST));

    userRepository.delete(userEntity);
  }
}
