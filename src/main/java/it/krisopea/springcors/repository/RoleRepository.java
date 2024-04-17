package it.krisopea.springcors.repository;

import it.krisopea.springcors.repository.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {
  RoleEntity findByName(String name);

  @Query("SELECT r FROM RoleEntity r WHERE r.name NOT IN ('FOUNDER')")
  List<RoleEntity> findAllRolesNotFounder();
}
