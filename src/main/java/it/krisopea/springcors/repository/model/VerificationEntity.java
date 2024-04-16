package it.krisopea.springcors.repository.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

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
}
