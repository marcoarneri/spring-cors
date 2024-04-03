package it.krisopea.springcors.repository.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity
@Table(name = "ROLE_ENTITY")
@NoArgsConstructor
public class RoleEntity {
  @Id
  @UuidGenerator
  @Column(name = "ID", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "NAME", nullable = false)
  private String name;
}
