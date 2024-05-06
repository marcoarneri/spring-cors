package it.krisopea.springcors.repository;

import it.krisopea.springcors.repository.model.VueClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VueClientRepository extends JpaRepository<VueClientEntity, Long>{
}
