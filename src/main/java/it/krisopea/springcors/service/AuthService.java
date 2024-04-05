package it.krisopea.springcors.service;

import it.krisopea.springcors.exception.AppErrorCodeMessageEnum;
import it.krisopea.springcors.repository.UserRepository;
import it.krisopea.springcors.repository.mapper.MapperUserEntity;
import it.krisopea.springcors.repository.model.UserEntity;
import it.krisopea.springcors.service.dto.request.UserLoginRequestDto;
import it.krisopea.springcors.service.dto.request.UserRegistrationRequestDto;
import it.krisopea.springcors.util.constant.RoleConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
//  private final UserRepository userRepository;
//  private final MapperUserEntity mapperUserEntity;
//  private final PasswordEncoder passwordEncoder;
//  private final ProducerTemplate producerTemplate;
//
//  public Boolean register(UserRegistrationRequestDto userRegistrationRequestDto) {
//
//    if (userRepository.findByUsername(userRegistrationRequestDto.getUsername()).isPresent()) {
//      log.error("Registration failed: {}", AppErrorCodeMessageEnum.USER_EXISTS);
//      return false;
//    }
//
//    UserEntity userEntity = mapperUserEntity.toUserEntity(userRegistrationRequestDto);
//
//    //TODO da fare in mapper
//    userEntity.setRole(RoleConstants.ROLE_USER);
//    userEntity.setEnabled(true);
//
//    userRepository.saveAndFlush(userEntity);
//    producerTemplate.sendBodyAndHeader(
//        "direct:sendRegistrationEmail", null, "email", userRegistrationRequestDto.getEmail());
//
//    return true;
//  }
//
//  public Boolean login(UserLoginRequestDto userLoginRequestDto) {
//
//    UserEntity userEntity =
//        userRepository.findByUsername(userLoginRequestDto.getUsername()).orElse(null);
//
//    if (userEntity == null) {
//      log.error("Authentication failed: {}", AppErrorCodeMessageEnum.BAD_REQUEST);
//      return false;
//    }
//
//    if (!(passwordEncoder.matches(userLoginRequestDto.getPassword(), userEntity.getPassword()))) {
//      log.error("Authentication failed: {}", AppErrorCodeMessageEnum.PASSWORD_MISMATCH);
//      return false;
//    }
//
//    return true;
//  }
}
