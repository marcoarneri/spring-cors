package it.krisopea.springcors.repository;

import it.krisopea.springcors.repository.model.VerificationEntity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRepository extends JpaRepository<VerificationEntity, UUID> {
  Optional<VerificationEntity> findByToken(String token);

  @Query("SELECT v FROM VerificationEntity v WHERE v.userEntity.username = :username")
  Optional<VerificationEntity> findByUsername(String username);

  @Query("SELECT v FROM VerificationEntity v WHERE v.createOn <= :lastUsefulDate")
  List<VerificationEntity> findByCreateOn(Instant lastUsefulDate);
}
