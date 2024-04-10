package it.krisopea.springcors.repository.model;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.UUID;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity
@Table(name = "ROLE_ENTITY")
public class RoleEntity {
  @Id
  @UuidGenerator
  @Column(name = "ID")
  private UUID id;

  @Column(name = "NAME")
  private String name;

  @ManyToMany(mappedBy = "ROLES")
  private Collection<UserEntity> users;

  @ManyToMany
  @JoinTable(
      name = "ROLES_PRIVILEGES",
      joinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID"),
      inverseJoinColumns = @JoinColumn(name = "PRIVILEGE_ID", referencedColumnName = "ID"))
  private Collection<PrivilegeEntity> privileges;
}
