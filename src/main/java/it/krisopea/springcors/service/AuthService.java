package it.krisopea.springcors.service;

import it.krisopea.springcors.exception.AppErrorCodeMessageEnum;
import it.krisopea.springcors.repository.UserRepository;
import it.krisopea.springcors.repository.mapper.MapperUserEntity;
import it.krisopea.springcors.repository.model.UserEntity;
import it.krisopea.springcors.service.dto.request.UserLoginRequestDto;
import it.krisopea.springcors.service.dto.request.UserRegistrationRequestDto;
import it.krisopea.springcors.util.constant.EmailEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
  private final UserRepository userRepository;
  private final MapperUserEntity mapperUserEntity;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final ProducerTemplate producerTemplate;

  public Boolean register(UserRegistrationRequestDto userRegistrationRequestDto) {

    if (userRepository.findByUsername(userRegistrationRequestDto.getUsername()).isPresent()) {
      log.error("Registration failed: {}", AppErrorCodeMessageEnum.USER_EXISTS);
      return false;
    }

    UserEntity userEntity = mapperUserEntity.toUserEntity(userRegistrationRequestDto);

    Map<String, Object> headers = new HashMap<>();
    headers.put("email", userRegistrationRequestDto.getEmail());
    headers.put("topic", EmailEnum.REGISTRATION);

    userRepository.saveAndFlush(userEntity);
    producerTemplate.sendBodyAndHeaders("direct:sendEmail", null, headers);
    return true;
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

    Map<String, Object> headers = new HashMap<>();
    headers.put("email", userEntity.getEmail());
    headers.put("loginTime", Instant.now().toString());
    headers.put("topic", EmailEnum.LOGIN);

    producerTemplate.sendBodyAndHeaders("direct:sendEmail", null, headers);

    authenticate(userLoginRequestDto.getUsername(), userLoginRequestDto.getPassword());

  }

  public void authenticate(String username, String password) {
    UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(username, password);
    Authentication authentication = authenticationManager.authenticate(authenticationToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

}
