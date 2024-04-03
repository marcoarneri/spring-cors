package it.krisopea.springcors.repository.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Data
@Entity
@Table(name = "USER_ENTITY")
@NoArgsConstructor
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "USERNAME", unique = true, nullable = false)
  private String username;

  @Column(name = "PASSWORD", nullable = false)
  private String password;

  @CreationTimestamp
  @Column(name = "CREATE_ON")
  private Instant createOn;

  @UpdateTimestamp
  @Column(name = "UPDATE_ON")
  private Instant updateOn;
}
