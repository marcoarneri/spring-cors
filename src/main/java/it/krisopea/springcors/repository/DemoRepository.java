package it.krisopea.springcors.repository;

import it.krisopea.springcors.repository.model.DemoPOJO;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface DemoRepository extends MongoRepository<DemoPOJO, Long> {
    boolean existsByIuv(String iuv);
}

