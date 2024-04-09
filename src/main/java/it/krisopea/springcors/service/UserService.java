package it.krisopea.springcors.service;

import it.krisopea.springcors.exception.AppErrorCodeMessageEnum;
import it.krisopea.springcors.exception.AppException;
import it.krisopea.springcors.repository.UserRepository;
import it.krisopea.springcors.repository.model.UserEntity;
import it.krisopea.springcors.service.dto.request.UserDeleteRequestDto;
import it.krisopea.springcors.service.dto.request.UserUpdateRequestDto;
import it.krisopea.springcors.util.constant.EmailEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ProducerTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final ProducerTemplate producerTemplate;
  private final AuthService authService;

  public Boolean updateUser(UserUpdateRequestDto requestDto) {
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
        if (requestDto.getPassword().isBlank()) {
          userEntity.setPassword(passwordEncoder.encode(requestDto.getPassword()));
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

      Map<String, Object> headers = new HashMap<>();
      headers.put("email", userEntity.getEmail());
      headers.put("updateTime", Instant.now().toString());
      headers.put("topic", EmailEnum.UPDATE);
      producerTemplate.sendBodyAndHeader("direct:sendEmail", null, headers);

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

    Map<String, Object> headers = new HashMap<>();
    headers.put("email", userEntity.getEmail());
    headers.put("deleteTime", Instant.now().toString());
    headers.put("isAdmin", Boolean.FALSE.toString());
    headers.put("topic", EmailEnum.DELETE);
    producerTemplate.sendBodyAndHeader("direct:sendEmail", null, headers);
  }

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
