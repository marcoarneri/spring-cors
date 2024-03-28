# Jpa

## Passaggi

Segui questi passaggi per aggiungere JPA al tuo progetto Spring Boot:

### 1. Aggiunta delle Dipendenze Necessarie

- Per iniziare, aggiungi la seguente dipendenza al tuo file `pom.xml` per integrare JPA nel tuo progetto:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```
***
### 2. Creazione delle Entity
Le entità sono oggetti Java che corrispondono direttamente alle tabelle del database.

- Crea le entità utilizzando l'annotazione `@Entity` ([DemoEntity.java](src%2Fmain%2Fjava%2Feu%2Ftasgroup%2Fspringbootguide%2Fentity%2FDemoEntity.java)) e definisci le proprietà della tabella come attributi della classe.
- Un'entità deve avere un identificatore univoco che la distingua da altre istanze della stessa classe. Questo è solitamente rappresentato da un campo annotato con `@Id`.
-  Gli attributi di un'entità rappresentano le colonne della tabella nel database. Ogni attributo può essere annotato con varie annotazioni JPA per definire il mapping dei dati.
***
### 3. Creazione del Repository
Il repository è un'interfaccia che estende `JpaRepository` e fornisce metodi per interagire con il database.

- Crea un'interfaccia di repository che estende `JpaRepository<Entity, ID>`, es. ([DemoRepository.java](src%2Fmain%2Fjava%2Feu%2Ftasgroup%2Fspringbootguide%2Frepository%2FDemoRepository.java)), dove Entity è il tipo dell'entità e ID è il tipo della chiave primaria.
- All'interno dell'interfaccia repository, puoi definire metodi per eseguire operazioni di base come il recupero, l'inserimento, l'aggiornamento e l'eliminazione delle entità nel database.
***
### 4. Scrivere Query con JpaRepository

- Query Metodi Derivati: JpaRepository supporta la creazione di query mediante metodi derivati dal nome del metodo.

```
DemoEntity findByIuv(String iuv);
```

- JPQL (Java Persistence Query Language): le query JPQL operano sugli oggetti delle entità definite nelle classi Java e sono indipendenti dal database sottostante.

```
@Query("SELECT d FROM DemoEntity d WHERE d.iuv = :iuv")
DemoEntity findByIuvJPQL(@Param("iuv") String iuv);
```

- Native Query: le query SQL nativa operano direttamente sulle tabelle e i dati nel database. Esiste il rischio che le query native non siano più compatibili se si cambia il tipo di database o la struttura del database stesso. Di conseguenza, è generalmente consigliato utilizzare query JPQL quando possibile.

```
@Query(value = "SELECT * FROM DEMO WHERE IUV = :iuv", nativeQuery = true)
DemoEntity findByIuvNativeQuery(@Param("iuv") String iuv);
```
***
### 5. Scrivere Specification con JpaSpecificationExecutor
Le Specification consentono di definire criteri di ricerca basati su condizioni dinamiche che possono essere combinate in modo flessibile durante l'esecuzione delle query.
Questo è particolarmente utile quando si devono gestire query complesse o quando le condizioni di ricerca possono variare in base alle esigenze dell'applicazione o degli utenti.

- **Estensione del Repository:** estendi la tua interfaccia repository con `JpaSpecificationExecutor`. Questa interfaccia permette di eseguire query basate sulle Specifiche, esempio ([DemoRepository.java](src%2Fmain%2Fjava%2Feu%2Ftasgroup%2Fspringbootguide%2Frepository%2FDemoRepository.java)).
- **Creazione delle Specifiche:** crea una classe dedicata per definire le Specifiche. Questa classe può contenere metodi statici che restituiscono oggetti Specification<T>, ognuno dei quali rappresenta una condizione di ricerca specifica, esempio ([DemoSpecifications.java](src%2Fmain%2Fjava%2Feu%2Ftasgroup%2Fspringbootguide%2Frepository%2Fspecification%2FDemoSpecifications.java)).
- **Combinazione delle Specifiche:** utilizza metodi come `and()`, `or()` per combinare le Specifiche e definire criteri di ricerca complessi.
- **Utilizzo delle Specifiche nei Servizi:** utilizza le Specifiche nei tuoi servizi o controller per eseguire query dinamiche basate sui criteri definiti, esempio ([DemoService.java](src%2Fmain%2Fjava%2Feu%2Ftasgroup%2Fspringbootguide%2Fservice%2FDemoService.java)).
