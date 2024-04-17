package it.krisopea.springcors.repository.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.Collection;
import java.util.UUID;

@Data
@Entity
@Table(name = "PRIVILEGE_ENTITY")
public class PrivilegeEntity {
  @Id
  @UuidGenerator
  @Column(name = "ID")
  private UUID id;

  @Column(name = "NAME")
  private String name;

  @ManyToMany(mappedBy = "privileges")
  private Collection<RoleEntity> roles;
}
