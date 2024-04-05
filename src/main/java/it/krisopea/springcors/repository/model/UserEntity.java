package it.krisopea.springcors.repository.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity
@Table(name = "USER_INFO")
@NoArgsConstructor
public class UserEntity {

  @Id
  @UuidGenerator
  @Column(name = "ID")
  private UUID id;

  @Column(name = "NAME", nullable = false)
  private String name;

  @Column(name = "SURNAME", nullable = false)
  private String surname;

  @Column(name = "EMAIL", nullable = false)
  private String email;

  @Column(name = "PICTURE")
  private String picture;

  @Column(name = "ROLE", nullable = false)
  private String role;

  @CreationTimestamp
  @Column(name = "CREATE_ON")
  private Instant createOn;

  @UpdateTimestamp
  @Column(name = "UPDATE_ON")
  private Instant updateOn;
}
