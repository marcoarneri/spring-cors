package it.krisopea.springcors.repository.mapper;

import it.krisopea.springcors.controller.model.request.UserRegistrationRequest;
import it.krisopea.springcors.controller.model.request.UserUpdateRequest;
import it.krisopea.springcors.repository.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class MapperUserEntity {

  @Mapping(target = "password", source = "password", qualifiedByName = "encodePassword")
  @Mapping(target = "enabled", ignore = true)
  @Mapping(target = "roles", ignore = true)
  public abstract UserEntity toUserEntity(UserRegistrationRequest request);

  @Mapping(target = "oldPassword", source = "password")
  @Mapping(target = "password", ignore = true)
  public abstract UserUpdateRequest toUpdateRequest(UserEntity userEntity);

  @Named("encodePassword")
  public String encodePassword(String password) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    return encoder.encode(password);
  }
}
