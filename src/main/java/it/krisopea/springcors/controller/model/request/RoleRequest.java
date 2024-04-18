package it.krisopea.springcors.controller.model.request;

import it.krisopea.springcors.repository.model.PrivilegeEntity;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleRequest {

  private String name;
  private List<PrivilegeEntity> privileges;
}
