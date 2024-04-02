package it.krisopea.springcors.util;

import it.krisopea.springcors.controller.model.request.UserRegistrationRequest;
import it.krisopea.springcors.service.dto.request.UserRegistrationRequestDto;
import org.mapstruct.MappingTarget;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class MapperUtils {
  private MapperUtils() {}

  // FIXME utile?
  public static void encodePassword(
      @MappingTarget UserRegistrationRequestDto userRegistrationRequestDto,
      UserRegistrationRequest userRegistrationRequest,
      BCryptPasswordEncoder passwordEncoder) {
    userRegistrationRequestDto.setPassword(
        passwordEncoder.encode(userRegistrationRequest.getPassword()));
  }
}
