package it.krisopea.springcors.repository;

import it.krisopea.springcors.repository.model.DemoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.io.Serializable;


public interface DemoRepository extends MongoRepository<DemoEntity, Serializable> {
    @Query(value = "{ 'iuv' : ?0 }", exists = true)
    boolean duplicateIuv(String iuv);


}

