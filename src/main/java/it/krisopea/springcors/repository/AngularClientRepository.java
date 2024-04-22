package it.krisopea.springcors.repository;

import it.krisopea.springcors.repository.model.AngularClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AngularClientRepository extends JpaRepository<AngularClientEntity, Long>{
}
