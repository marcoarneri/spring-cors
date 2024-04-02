package it.krisopea.springcors.repository;

import it.krisopea.springcors.repository.model.DemoUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UserRepository
        extends JpaRepository<DemoUserEntity, Long>, JpaSpecificationExecutor<DemoUserEntity> {

//    Metodo di query derivato: Questo è il metodo più comune e più semplice. In questo approccio, scrivi un metodo all'interno
//    dell'interfaccia del repository con un nome specifico seguendo le convenzioni di denominazione di Spring Data JPA.
//    Il framework si occupa di interpretare il nome del metodo e genera automaticamente la query corrispondente. Ad esempio,
//    findByIuv(String iuv) è un metodo di query derivato, che cerca un'entità in base al suo attributo iuv.
    DemoUserEntity findByIuvAndNoticeId(String iuv, String noticeId);
    Optional<DemoUserEntity> findByIuv(String iuv);

    @Query("SELECT u FROM DemoEntity u WHERE u.location = :location")
    List<DemoUserEntity> findAllByLocation(@Param("location") String location);
//    Annotazione @Query: Questo approccio consente di scrivere query personalizzate in JPQL (Java Persistence Query Language)
//    o SQL direttamente sopra il metodo del repository utilizzando l'annotazione @Query. Questo fornisce maggiore flessibilità
//    nel definire query complesse che non possono essere gestite facilmente tramite metodi di query derivati. Questo è utile
//    soprattutto quando hai bisogno di condizioni di ricerca più complesse, join tra entità o query particolarmente ottimizzate.
}


//    Operazioni CRUD di base: JpaRepository estende CrudRepository, il che significa che erediti automaticamente i metodi per le
//    operazioni CRUD (Create, Read, Update, Delete). Questi metodi includono save, findAll, findById, delete, ecc. Anche se la tua
//    classe repository è vuota, hai ancora accesso a queste operazioni di base attraverso l'ereditarietà.
