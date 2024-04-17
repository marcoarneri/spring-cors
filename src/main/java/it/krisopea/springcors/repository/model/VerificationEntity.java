package it.krisopea.springcors.repository.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "VERIFICATION_ENTITY")
public class VerificationEntity {
  @Id
  @UuidGenerator
  @Column(name = "VERIFICATION_ID")
  private UUID id;

  @Column(name = "ATTEMPTS")
  private Integer attempts;

  @Column(name = "TOKEN")
  private String token;

  @OneToOne
  @JoinColumn(name = "USERNAME", referencedColumnName = "USERNAME")
  private UserEntity userEntity;

  @Column(name = "LAST_SENT")
  private Instant lastSent;

  @CreationTimestamp
  @Column(name = "CREATE_ON")
  private Instant createOn;
}
