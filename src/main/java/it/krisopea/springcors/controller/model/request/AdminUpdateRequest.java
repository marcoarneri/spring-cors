package it.krisopea.springcors.controller.model.request;

import it.krisopea.springcors.repository.model.RoleEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdminUpdateRequest {
  private String username;

  private String email;

  private List<RoleEntity> roles;
}
