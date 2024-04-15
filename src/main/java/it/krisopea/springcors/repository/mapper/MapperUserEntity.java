package it.krisopea.springcors.repository.mapper;

import it.krisopea.springcors.controller.model.request.AdminUpdateRequest;
import it.krisopea.springcors.controller.model.request.UserUpdateRequest;
import it.krisopea.springcors.repository.model.UserEntity;
import it.krisopea.springcors.service.dto.request.UserRegistrationRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class MapperUserEntity {

  @Mapping(target = "password", source = "password", qualifiedByName = "encodePassword")
  @Mapping(target = "roles", ignore = true)
  @Mapping(target = "enabled", constant = "false")
  @Mapping(target = "accountNonExpired", constant = "false")
  @Mapping(target = "credentialsNonExpired", constant = "false")
  @Mapping(target = "accountNonLocked", constant = "false")
  public abstract UserEntity toUserEntity(UserRegistrationRequestDto request);

  @Mapping(target = "oldPassword", source = "password")
  @Mapping(target = "password", ignore = true)
  public abstract UserUpdateRequest toUpdateRequest(UserEntity userEntity);

  @Named("encodePassword")
  public String encodePassword(String password) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    return encoder.encode(password);
  }

  public abstract AdminUpdateRequest toAdminUpdateRequest(UserEntity user);
}
