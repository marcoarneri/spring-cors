package it.krisopea.springcors.repository;

import it.krisopea.springcors.repository.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository
    extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

}
