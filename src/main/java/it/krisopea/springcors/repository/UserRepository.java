package it.krisopea.springcors.repository;

import it.krisopea.springcors.repository.model.UserEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

// TODO da fare la barra di ricerca utilizzare lo specificationexecutor

@Repository
public interface UserRepository
    extends JpaRepository<UserEntity, UUID>, JpaSpecificationExecutor<UserEntity> {
  Optional<UserEntity> findByUsername(String username);

  @Query("SELECT u FROM UserEntity u JOIN u.roles r WHERE r.name = 'USER'")
  List<UserEntity> findAllUsers();

  @Query("SELECT u FROM UserEntity u JOIN u.roles r WHERE r.name = :name")
  List<UserEntity> findUsersByRoleName(String name);

  @Query(
      "SELECT u FROM UserEntity u WHERE NOT EXISTS (SELECT r FROM u.roles r WHERE r.name ="
          + " 'FOUNDER')")
  List<UserEntity> findAllNotFounder();

  Optional<UserEntity> findByEmail(String email);
}
