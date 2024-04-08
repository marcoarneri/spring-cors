package it.krisopea.springcors.service;

import static org.apache.logging.log4j.util.Strings.isBlank;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

import it.krisopea.springcors.exception.AppErrorCodeMessageEnum;
import it.krisopea.springcors.exception.AppException;
import it.krisopea.springcors.repository.UserRepository;
import it.krisopea.springcors.repository.model.UserEntity;
import it.krisopea.springcors.service.dto.request.UserDeleteRequestDto;
import it.krisopea.springcors.service.dto.request.UserUpdateRequestDto;
import it.krisopea.springcors.util.annotation.IsAdmin;
import it.krisopea.springcors.util.annotation.IsAuthenticated;
import it.krisopea.springcors.util.constant.EmailEnum;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired private ProducerTemplate producerTemplate;

  @IsAuthenticated
  public void updateUser(UserUpdateRequestDto userUpdateRequestDto, String username) {
    UserEntity userEntity =
        userRepository
            .findByUsername(username)
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

    Map<String, Object> headers = new HashMap<>();
    headers.put("email", userEntity.getEmail());
    headers.put("updateTime", Instant.now().toString());
    headers.put("topic", EmailEnum.UPDATE);
    producerTemplate.sendBodyAndHeader("direct:sendEmail", null, headers);
  }

  @IsAuthenticated
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

    Map<String, Object> headers = new HashMap<>();
    headers.put("email", userEntity.getEmail());
    headers.put("deleteTime", Instant.now().toString());
    headers.put("isAdmin", Boolean.FALSE.toString());
    headers.put("topic", EmailEnum.DELETE);
    producerTemplate.sendBodyAndHeader("direct:sendEmail", null, headers);
  }

  @IsAdmin
  public void deleteUser(String username) {
    UserEntity userEntity =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new AppException(AppErrorCodeMessageEnum.BAD_REQUEST));

    userRepository.delete(userEntity);

    Map<String, Object> headers = new HashMap<>();
    headers.put("email", userEntity.getEmail());
    headers.put("deleteTime", Instant.now().toString());
    headers.put("isAdmin", Boolean.TRUE.toString());
    headers.put("topic", EmailEnum.DELETE);
    producerTemplate.sendBodyAndHeader("direct:sendEmail", null, headers);
  }
}
