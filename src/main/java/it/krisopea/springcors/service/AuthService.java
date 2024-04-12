package it.krisopea.springcors.service;

import it.krisopea.springcors.exception.AppErrorCodeMessageEnum;
import it.krisopea.springcors.exception.AppException;
import it.krisopea.springcors.repository.RoleRepository;
import it.krisopea.springcors.repository.UserRepository;
import it.krisopea.springcors.repository.VerificationRepository;
import it.krisopea.springcors.repository.mapper.MapperUserEntity;
import it.krisopea.springcors.repository.model.RoleEntity;
import it.krisopea.springcors.repository.model.UserEntity;
import it.krisopea.springcors.repository.model.VerificationEntity;
import it.krisopea.springcors.service.dto.request.UserLoginRequestDto;
import it.krisopea.springcors.service.dto.request.UserRegistrationRequestDto;
import it.krisopea.springcors.util.constant.EmailEnum;
import it.krisopea.springcors.util.constant.RoleConstants;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final VerificationRepository verificationRepository;
  private final MapperUserEntity mapperUserEntity;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final ProducerTemplate producerTemplate;

  @Value("${verification.maxAttempts}")
  private int maxAttempts;

  public Boolean register(UserRegistrationRequestDto userRegistrationRequestDto) {

    if (userRepository.findByUsername(userRegistrationRequestDto.getUsername()).isPresent()) {
      log.error("Registration failed: {}", AppErrorCodeMessageEnum.USER_EXISTS);
      return false;
    }

    UserEntity userEntity = mapperUserEntity.toUserEntity(userRegistrationRequestDto);
    RoleEntity adminRole = roleRepository.findByName(RoleConstants.ROLE_USER);
    userEntity.setRoles(Collections.singletonList(adminRole));
    userEntity.setEnabled(Boolean.TRUE);
    userRepository.saveAndFlush(userEntity);
    sendEmail(userEntity, EmailEnum.REGISTRATION);
    setupVerification(userEntity);
    return true;
  }

  public void setupVerification(UserEntity userEntity) {
    UUID token = UUID.randomUUID();
    VerificationEntity verificationEntity = new VerificationEntity();
    verificationEntity.setUserEntity(userEntity);
    verificationEntity.setToken(token);
    verificationEntity.setAttempts(0);

    verificationRepository.saveAndFlush(verificationEntity);
    sendEmail(userEntity, EmailEnum.VERIFY);
  }

  public Integer sendVerificationEmail(String username) {
    UserEntity userEntity =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new AppException(AppErrorCodeMessageEnum.BAD_REQUEST));
    VerificationEntity verificationEntity =
        verificationRepository
            .findByUserUsername(userEntity.getUsername())
            .orElseThrow(() -> new AppException(AppErrorCodeMessageEnum.BAD_REQUEST));
    int count = verificationEntity.getAttempts();
    if (count >= 5) {
      return -1;
    }
    verificationEntity.setAttempts(++count);
    sendEmail(userEntity, EmailEnum.VERIFY);
    verificationRepository.saveAndFlush(verificationEntity);
    return maxAttempts - verificationEntity.getAttempts();
  }

  public void login(UserLoginRequestDto userLoginRequestDto) {

    UserEntity userEntity =
        userRepository.findByUsername(userLoginRequestDto.getUsername()).orElse(null);

    if (userEntity == null) {
      log.error("Authentication failed: {}", AppErrorCodeMessageEnum.BAD_REQUEST);
      return;
    }

    if (!(passwordEncoder.matches(userLoginRequestDto.getPassword(), userEntity.getPassword()))) {
      log.error("Authentication failed: {}", AppErrorCodeMessageEnum.PASSWORD_MISMATCH);
      return;
    }
    authenticate(userLoginRequestDto.getUsername(), userLoginRequestDto.getPassword());
    sendEmail(userEntity, EmailEnum.LOGIN);
  }

  public void authenticate(String username, String password) {
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(username, password);
    Authentication authentication = authenticationManager.authenticate(authenticationToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  public void sendEmail(UserEntity userEntity, EmailEnum action) {
    Map<String, Object> headers = new HashMap<>();
    headers.put("to", userEntity.getEmail());
    if (action == EmailEnum.REGISTRATION) {
      headers.put("topic", EmailEnum.REGISTRATION);
    } else if (action == EmailEnum.LOGIN) {
      headers.put("loginTime", Instant.now().toString());
      headers.put("topic", EmailEnum.LOGIN);
    } else if (action == EmailEnum.VERIFY) {
      headers.put("topic", EmailEnum.VERIFY);
    }
    producerTemplate.sendBodyAndHeaders("direct:sendEmail", null, headers);
  }
}
