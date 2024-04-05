package it.krisopea.springcors.repository.mapper;

import it.krisopea.springcors.repository.model.UserEntity;
import it.krisopea.springcors.service.dto.request.UserLoginRequestDto;
import it.krisopea.springcors.service.dto.request.UserRegistrationRequestDto;
import org.mapstruct.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class MapperUserEntity {

  public abstract UserEntity toUserEntity(UserLoginRequestDto requestDto);

//  @Mapping(target = "password", source = "password", qualifiedByName = "encodePassword")
//  public abstract UserEntity toUserEntity(UserRegistrationRequestDto requestDto);
//
//  @Named("encodePassword")
//  public String encodePassword(String password) {
//    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//    return encoder.encode(password);
//  }
}
