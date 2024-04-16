package it.krisopea.springcors.service;

import it.krisopea.springcors.exception.AppErrorCodeMessageEnum;
import it.krisopea.springcors.exception.AppException;
import it.krisopea.springcors.repository.RoleRepository;
import it.krisopea.springcors.repository.UserRepository;
import it.krisopea.springcors.repository.VerificationRepository;
import it.krisopea.springcors.repository.model.RoleEntity;
import it.krisopea.springcors.repository.model.UserEntity;
import it.krisopea.springcors.repository.model.VerificationEntity;
import it.krisopea.springcors.service.dto.request.UserDeleteRequestDto;
import it.krisopea.springcors.service.dto.request.UserUpdateRequestDto;
import it.krisopea.springcors.util.UuidUtil;
import it.krisopea.springcors.util.constant.EmailEnum;
import it.krisopea.springcors.util.constant.RoleConstants;
import java.time.Instant;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
  private final UserRepository userRepository;
  private final VerificationRepository verificationRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final ProducerTemplate producerTemplate;
  private final AuthService authService;

  public Boolean updateUser(UserUpdateRequestDto requestDto) {
    if (userRepository.findByUsername(requestDto.getUsername()).isPresent()
        && !(SecurityContextHolder.getContext()
            .getAuthentication()
            .getName()
            .equals(requestDto.getUsername()))) {
      throw new AppException(AppErrorCodeMessageEnum.USER_EXISTS);
    }

    UserEntity userEntity =
        userRepository
            .findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
            .orElseThrow(() -> new AppException(AppErrorCodeMessageEnum.BAD_REQUEST));

    if (!(requestDto.getOldPassword().isBlank())) {
      if (passwordEncoder.matches(requestDto.getOldPassword(), userEntity.getPassword())) {
        userEntity.setName(requestDto.getName());
        userEntity.setSurname(requestDto.getSurname());
        userEntity.setUsername(requestDto.getUsername());
        userEntity.setEmail(requestDto.getEmail());
        if (!requestDto.getPassword().isBlank()) {
          userEntity.setPassword(passwordEncoder.encode(requestDto.getPassword()));
          userRepository.saveAndFlush(userEntity);
          return true;
        } else {
          userEntity.setPassword(userEntity.getPassword());
          authService.authenticate(requestDto.getUsername(), requestDto.getPassword());
        }

        userRepository.saveAndFlush(userEntity);
      } else {
        throw new AppException(AppErrorCodeMessageEnum.PASSWORD_MISMATCH);
      }
    } else {
      userEntity.setName(requestDto.getName());
      userEntity.setSurname(requestDto.getSurname());
      userEntity.setUsername(requestDto.getUsername());
      userEntity.setEmail(requestDto.getEmail());

      userRepository.saveAndFlush(userEntity);
      return true;
    }

//    sendEmail(userEntity, EmailEnum.UPDATE);
    return false;
  }

  public void deleteUser(UserDeleteRequestDto userDeleteRequestDto, String username) {
    UserEntity userEntity =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new AppException(AppErrorCodeMessageEnum.BAD_REQUEST));

    String encryptedPassword = passwordEncoder.encode(userDeleteRequestDto.getPassword());

    if (!encryptedPassword.equals(userEntity.getPassword())) {
      throw new AppException(AppErrorCodeMessageEnum.BAD_REQUEST);
    }

    userRepository.delete(userEntity);
//    sendEmail(userEntity, EmailEnum.DELETE);
  }

  public Boolean verifyUser(String tokenString) {

    Optional<VerificationEntity> optionalVerificationEntity =
        verificationRepository.findByToken(tokenString);
    if (optionalVerificationEntity.isEmpty()) {
      return false;
    }

    VerificationEntity verificationEntity = optionalVerificationEntity.get();
    if (!tokenString.equals(verificationEntity.getToken())) {
      return false;
    }

    RoleEntity roleEntity = roleRepository.findByName(RoleConstants.ROLE_VERIFIED);
    UserEntity userEntity = verificationEntity.getUserEntity();
    if (userEntity == null) {
      throw new AppException(AppErrorCodeMessageEnum.BAD_REQUEST);
    }
    List<RoleEntity> userRoles = new ArrayList<>();
    userRoles.add(roleEntity);
    userEntity.setRoles(userRoles);
    userEntity.setEnabled(Boolean.TRUE);
    userEntity.setAccountNonExpired(Boolean.TRUE);
    userEntity.setAccountNonLocked(Boolean.TRUE);
    userEntity.setCredentialsNonExpired(Boolean.TRUE);
    verificationRepository.delete(verificationEntity);
    userRepository.save(userEntity);
    return true;
  }

  public void sendEmail(UserEntity userEntity, EmailEnum action) {
    Map<String, Object> headers = new HashMap<>();
    headers.put("to", userEntity.getEmail());
    if (action == EmailEnum.UPDATE) {
      headers.put("updateTime", Instant.now().toString());
      headers.put("topic", EmailEnum.UPDATE);
    } else if (action == EmailEnum.DELETE) {
      headers.put("deleteTime", Instant.now().toString());
      headers.put("isAdmin", Boolean.TRUE.toString());
      headers.put("topic", EmailEnum.DELETE);
    }
    producerTemplate.sendBodyAndHeaders("direct:sendEmail", null, headers);
  }
}
