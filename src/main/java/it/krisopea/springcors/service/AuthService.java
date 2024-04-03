package it.krisopea.springcors.service;

import it.krisopea.springcors.exception.AppErrorCodeMessageEnum;
import it.krisopea.springcors.exception.AppException;
import it.krisopea.springcors.repository.RoleRepository;
import it.krisopea.springcors.repository.UserRepository;
import it.krisopea.springcors.repository.mapper.MapperUserEntity;
import it.krisopea.springcors.repository.model.RoleEntity;
import it.krisopea.springcors.repository.model.UserEntity;
import it.krisopea.springcors.service.dto.request.UserLoginRequestDto;
import it.krisopea.springcors.service.dto.request.UserRegistrationRequestDto;
import it.krisopea.springcors.util.annotation.AllowAnonymous;
import it.krisopea.springcors.util.constant.RoleConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final MapperUserEntity mapperUserEntity;
  private final BCryptPasswordEncoder passwordEncoder;
  //  @Autowired FIXME
  private AuthenticationManager authenticationManager;

  @AllowAnonymous
  public void register(UserRegistrationRequestDto userRegistrationRequestDto) {
    if ((userRepository.findByEmail(userRegistrationRequestDto.getEmail()).isPresent())
        || (userRepository.findByUsername(userRegistrationRequestDto.getUsername()).isPresent())) {
      throw new AppException(AppErrorCodeMessageEnum.BAD_REQUEST);
    }

    UserEntity userEntity = mapperUserEntity.toUserEntity(userRegistrationRequestDto);

    RoleEntity userRole = roleRepository.findByName(RoleConstants.ROLE_USER).get();

    userEntity.setRole(userRole.getName());
    userRepository.saveAndFlush(userEntity);
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
