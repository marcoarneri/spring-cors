package it.krisopea.springcors.repository.mapper;

import it.krisopea.springcors.repository.model.UserEntity;
import it.krisopea.springcors.service.dto.request.UserRegistrationRequestDto;
import it.krisopea.springcors.util.constant.RoleConstants;
import org.mapstruct.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class MapperUserEntity {
  @Mapping(target = "password", source = "password", qualifiedByName = "encodePassword")
  @Mapping(target = "enabled", ignore = true)
  @Mapping(target = "role", ignore = true)
  public abstract UserEntity toUserEntity(UserRegistrationRequestDto requestDto);

  @AfterMapping
  protected void setDefaultValues(@MappingTarget UserEntity userEntity) {
    userEntity.setRole(RoleConstants.ROLE_USER);
    userEntity.setEnabled(Boolean.TRUE);
  }

  @Named("encodePassword")
  public String encodePassword(String password) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    return encoder.encode(password);
  }
}
