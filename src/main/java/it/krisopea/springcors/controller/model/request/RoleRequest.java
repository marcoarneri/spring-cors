package it.krisopea.springcors.controller.model.request;

import it.krisopea.springcors.repository.model.PrivilegeEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoleRequest {

  private String name;
  private List<PrivilegeEntity> privileges;
}
