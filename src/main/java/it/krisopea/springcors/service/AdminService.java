package it.krisopea.springcors.service;

import it.krisopea.springcors.exception.AppErrorCodeMessageEnum;
import it.krisopea.springcors.exception.AppException;
import it.krisopea.springcors.repository.UserRepository;
import it.krisopea.springcors.repository.model.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

  private final UserRepository userRepository;

  public void updateUser(UserEntity user) {
    UserEntity findUser =
        userRepository
            .findByUsername(user.getUsername())
            .orElseThrow(() -> new AppException(AppErrorCodeMessageEnum.BAD_REQUEST));

    findUser.setRole(user.getRole());
    userRepository.saveAndFlush(findUser);
  }
}
