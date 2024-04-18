package it.krisopea.springcors.service.dto.request;

import it.krisopea.springcors.repository.model.RoleEntity;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUpdateRequestDto {
  private String username;

  private String email;

  private List<RoleEntity> roles;
}
