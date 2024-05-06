package it.krisopea.springcors.repository;

import it.krisopea.springcors.repository.model.ReactClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactClientRepository extends JpaRepository<ReactClientEntity, Long>{
}
