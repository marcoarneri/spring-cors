package it.krisopea.springcors.service;

import static org.apache.logging.log4j.util.Strings.isBlank;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

import it.krisopea.springcors.controller.model.request.UserDeleteRequest;
import it.krisopea.springcors.exception.AppErrorCodeMessageEnum;
import it.krisopea.springcors.exception.AppException;
import it.krisopea.springcors.repository.UserRepository;
import it.krisopea.springcors.repository.model.UserEntity;
import it.krisopea.springcors.service.dto.request.UserUpdateRequestDto;
import it.krisopea.springcors.util.AuthenticatedUserUtils;
import it.krisopea.springcors.util.annotation.IsAdmin;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
  private final UserRepository userRepository;
  private final AuthenticatedUserUtils authenticatedUserUtils;
  private final BCryptPasswordEncoder passwordEncoder;

  @PreAuthorize("@authenticatedUserUtils.hasId(#userId)")
  public void updateUser(UserUpdateRequestDto userUpdateRequestDto, UUID userId) {
    UserEntity userEntity =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new AppException(AppErrorCodeMessageEnum.BAD_REQUEST));

    String name = userUpdateRequestDto.getName();
    String surname = userUpdateRequestDto.getSurname();
    String password = userUpdateRequestDto.getPassword();

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

    // TODO inviare email di aggiornamento all'utente
    userEntity.setUsername(null);
    userEntity.setEmail(null);
    userRepository.saveAndFlush(userEntity);
  }

  @PreAuthorize("@authenticatedUserUtils.hasId(#userId)")
  public void deleteUser(UserDeleteRequest userDeleteRequest, UUID userId) {
    UserEntity userEntity =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new AppException(AppErrorCodeMessageEnum.BAD_REQUEST));

    // TODO qua inviare email di notifica cancellazione tramite email passata nel dto
    String encryptedPassword = passwordEncoder.encode(userDeleteRequest.getPassword());

    if (!encryptedPassword.equals(userEntity.getPassword())) {
      throw new AppException(AppErrorCodeMessageEnum.BAD_REQUEST);
    } else {
      userRepository.delete(userEntity);
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
