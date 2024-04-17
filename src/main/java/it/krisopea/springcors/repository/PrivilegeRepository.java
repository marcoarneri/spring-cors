package it.krisopea.springcors.repository;

import it.krisopea.springcors.repository.model.PrivilegeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PrivilegeRepository extends JpaRepository<PrivilegeEntity, UUID> {
  PrivilegeEntity findByName(String name);
}
