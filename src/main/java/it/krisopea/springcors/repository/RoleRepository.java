package it.krisopea.springcors.repository;

import it.krisopea.springcors.repository.model.RoleEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {
  RoleEntity findByName(String name);
}
