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
import it.krisopea.springcors.util.constant.EmailEnum;
import it.krisopea.springcors.util.constant.RoleConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.*;

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

  public void sendEmail(UserEntity userEntity) {
    Map<String, Object> headers = new HashMap<>();
    headers.put("to", userEntity.getEmail());
    headers.put("topic", EmailEnum.CHANGE_PASSWORD);
    headers.put("id", userEntity.getId());

    producerTemplate.sendBodyAndHeaders("direct:sendEmail", null, headers);
  }

  public void sendEmailToChangePassword(String email) {
    Optional<UserEntity> userEntity = userRepository.findByEmail(email);
    if (userEntity.isEmpty()){
      throw new AppException(AppErrorCodeMessageEnum.EMAIL_NOT_EXIST);
    }
    sendEmail(userEntity.get());
  }

  public UserEntity verifyUserId(String id) {
    Optional<UserEntity> userEntity = userRepository.findById(UUID.fromString(id));
    if (userEntity.isEmpty()){
      throw new AppException(AppErrorCodeMessageEnum.ACCESS_DENIED);
    }
    return userEntity.get();
  }

  public boolean verifyPasswordMatch(String password1, String password2) {
      return password1.equals(password2);
  }

  public void updatePassword(String password1, UserEntity userEntity) {
    userEntity.setPassword(passwordEncoder.encode(password1));
    userRepository.save(userEntity);
    log.info("user: {}, update his password!", userEntity.getUsername());
  }

  public Pair<Boolean, UserEntity> isPasswordOld(String password1, String username) {
    Optional<UserEntity> userEntity = userRepository.findByUsername(username);
    if (userEntity.isEmpty()){
      throw new AppException(AppErrorCodeMessageEnum.USER_NOT_EXISTS);
    }
    if (passwordEncoder.matches(password1, userEntity.get().getPassword())){
      return Pair.of(Boolean.FALSE, null);
    }
    return Pair.of(Boolean.TRUE, userEntity.get());
  }
}
