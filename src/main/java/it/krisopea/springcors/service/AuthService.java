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
import java.time.Duration;
import java.time.Instant;
import java.util.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang3.RandomStringUtils;
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

  //  @Value("${verification.link}")
  //  private String baseUrl;

  public Boolean register(UserRegistrationRequestDto userRegistrationRequestDto) {

    if (userRepository.findByUsername(userRegistrationRequestDto.getUsername()).isPresent()) {
      log.error("Registration failed: {}", AppErrorCodeMessageEnum.USER_EXISTS);
      return false;
    }
    List<RoleEntity> userRoles = new ArrayList<>();
    UserEntity userEntity = mapperUserEntity.toUserEntity(userRegistrationRequestDto);
    RoleEntity role = roleRepository.findByName(RoleConstants.ROLE_USER);
    userRoles.add(role);
    userEntity.setRoles(userRoles);
    userEntity.setEnabled(Boolean.FALSE);
    UserEntity userEntitySaved = userRepository.save(userEntity);
    String token = setupVerification(userEntity);
    sendRegistrationEmail(userEntitySaved, token);
    return true;
  }

  private String setupVerification(UserEntity userEntity) {
    String token = RandomStringUtils.random(6, Boolean.TRUE, Boolean.TRUE);
    VerificationEntity verificationEntity = new VerificationEntity();
    verificationEntity.setUserEntity(userEntity);
    verificationEntity.setToken(token);
    verificationEntity.setAttempts(0);

    verificationRepository.save(verificationEntity);
    return token;
  }

  public Integer sendRegistrationEmail(UserEntity userEntity, String token) {
    VerificationEntity verificationEntity =
        verificationRepository
            .findByUsername(userEntity.getUsername())
            .orElseThrow(() -> new AppException(AppErrorCodeMessageEnum.BAD_REQUEST));

    if (verificationEntity.getLastSent() != null
        && Duration.between(verificationEntity.getLastSent(), Instant.now()).toHours() >= 24) {
      verificationEntity.setAttempts(0);
    }

    int count = verificationEntity.getAttempts();
    if (count >= 5) {
      return -1;
    }

    count++;
    if (verificationEntity.getLastSent() != null
        && Duration.between(verificationEntity.getLastSent(), Instant.now()).toMinutes()
            < 10L * count) {
      return -2;
    }

    verificationEntity.setAttempts(count);
    sendEmail(userEntity, EmailEnum.REGISTRATION, token);
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
//    sendEmail(userEntity, EmailEnum.LOGIN);
  }

  public void authenticate(String username, String password) {
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(username, password);
    Authentication authentication = authenticationManager.authenticate(authenticationToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  public void sendEmail(UserEntity userEntity, EmailEnum action, String token) {
    Map<String, Object> headers = new HashMap<>();
    headers.put("to", userEntity.getEmail());
    if (action == EmailEnum.REGISTRATION) {
      headers.put("topic", EmailEnum.REGISTRATION);
      headers.put("token", token);
      headers.put("username", userEntity.getUsername());
    } else if (action == EmailEnum.LOGIN) {
      headers.put("loginTime", Instant.now().toString());
      headers.put("topic", EmailEnum.LOGIN);
    }
    producerTemplate.sendBodyAndHeaders("direct:sendEmail", null, headers);
  }

  public void resendEmail(String username, String email) {
    Optional<UserEntity> userEntity = userRepository.findByUsername(username);
    userEntity.get().setEmail(email);
    userRepository.save(userEntity.get());
    Optional<VerificationEntity> verificationEntity = verificationRepository.findByUsername(username);
    sendRegistrationEmail(userEntity.get(), verificationEntity.get().getToken());
  }
}
