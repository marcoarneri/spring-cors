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

  // FIXME
  @Query("SELECT u FROM UserEntity u WHERE u.role = 'USER'")
  List<UserEntity> findAllUsers();

  @Query("SELECT u FROM UserEntity u WHERE u.role <> 'FOUNDER'")
  List<UserEntity> findAllNotFounder();
}
