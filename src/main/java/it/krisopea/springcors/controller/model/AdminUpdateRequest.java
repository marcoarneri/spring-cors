package it.krisopea.springcors.controller.model;

import it.krisopea.springcors.repository.model.RoleEntity;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUpdateRequest {
  private String username;

  private String email;

  private List<RoleEntity> roles;
}
