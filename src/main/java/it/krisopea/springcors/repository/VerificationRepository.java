package it.krisopea.springcors.repository;

import it.krisopea.springcors.repository.model.VerificationEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRepository extends JpaRepository<VerificationEntity, UUID> {
  Optional<VerificationEntity> findByToken(UUID token);

  @Query("SELECT v FROM VerificationEntity v WHERE v.userEntity.username = :username")
  Optional<VerificationEntity> findByUsername(String username);
}
