package it.krisopea.springcors.repository.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Collection;
import java.util.UUID;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity
@Table(name = "USER_ENTITY")
public class UserEntity {

  @Id
  @UuidGenerator
  @Column(name = "ID")
  private UUID id;

  @Column(name = "NAME", nullable = false)
  private String name;

  @Column(name = "SURNAME", nullable = false)
  private String surname;

  @Column(name = "USERNAME", unique = true, nullable = false)
  private String username;

  @Column(name = "EMAIL", nullable = false)
  private String email;

  @Column(name = "PASSWORD", nullable = false)
  private String password;

  @ManyToMany
  @JoinTable(
      name = "ROLES",
      joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"),
      inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID"))
  private Collection<RoleEntity> roles;

  @Column(name = "ENABLED", nullable = false)
  private boolean enabled;

  @CreationTimestamp
  @Column(name = "CREATE_ON")
  private Instant createOn;

  @UpdateTimestamp
  @Column(name = "UPDATE_ON")
  private Instant updateOn;
}
