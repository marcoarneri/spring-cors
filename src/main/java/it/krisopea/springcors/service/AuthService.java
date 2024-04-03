package it.krisopea.springcors.service;

import it.krisopea.springcors.exception.AppErrorCodeMessageEnum;
import it.krisopea.springcors.exception.AppException;
import it.krisopea.springcors.repository.UserRepository;
import it.krisopea.springcors.repository.mapper.MapperUserEntity;
import it.krisopea.springcors.repository.model.UserEntity;
import it.krisopea.springcors.service.dto.request.UserLoginRequestDto;
import it.krisopea.springcors.service.dto.request.UserRegistrationRequestDto;
import it.krisopea.springcors.util.annotation.AllowAnonymous;
import it.krisopea.springcors.util.constant.RoleConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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
  private final UserRepository userRepository;
  private final MapperUserEntity mapperUserEntity;
  private final PasswordEncoder passwordEncoder;
  //  @Autowired FIXME
  private AuthenticationManager authenticationManager;
  @Autowired private ProducerTemplate producerTemplate;

  @AllowAnonymous
  public void register(UserRegistrationRequestDto userRegistrationRequestDto) {
    if ((userRepository.findByEmail(userRegistrationRequestDto.getEmail()).isPresent())
        || (userRepository.findByUsername(userRegistrationRequestDto.getUsername()).isPresent())) {
      throw new AppException(AppErrorCodeMessageEnum.BAD_REQUEST);
    }

    UserEntity userEntity = mapperUserEntity.toUserEntity(userRegistrationRequestDto);

    userEntity.setRole(RoleConstants.ROLE_USER);
    userRepository.saveAndFlush(userEntity);
    producerTemplate.sendBodyAndHeader(
        "direct:sendRegistrationEmail", null, "email", userRegistrationRequestDto.getEmail());
  }

  @AllowAnonymous
  public void login(UserLoginRequestDto userLoginRequestDto) {
    UserEntity userEntity;
    if (userLoginRequestDto.getUsernameOrEmail().contains("@")) {
      userEntity =
          userRepository.findByEmail(userLoginRequestDto.getUsernameOrEmail()).orElse(null);
    } else {
      userEntity =
          userRepository.findByUsername(userLoginRequestDto.getUsernameOrEmail()).orElse(null);
    }

    if (userEntity == null) {
      throw new AppException(AppErrorCodeMessageEnum.BAD_REQUEST);
    }

    String encryptedPassword = passwordEncoder.encode(userLoginRequestDto.getPassword());

    if (!encryptedPassword.equals(userEntity.getPassword())) {
      throw new AppException(AppErrorCodeMessageEnum.BAD_REQUEST);
    }

    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                userLoginRequestDto.getUsernameOrEmail(), userLoginRequestDto.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
