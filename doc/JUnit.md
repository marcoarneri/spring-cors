# JUnit

## Passaggi

Segui questi passaggi per integrare JUnit nel tuo progetto Spring Boot:

### 1. Aggiunta delle Dipendenze Necessarie

- Aggiungi le seguenti dipendenze nel file pom.xml del tuo progetto Spring Boot per includere JUnit e il supporto per i test:

```xml
<dependencies>
<!--Questa dipendenza dovrebbe essere già presente nel tuo pom.xml-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
<!--Database leggero utilizzato per testare applicazioni Spring Boot    -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```
***
### 2. Creazione di Test JUnit
I test JUnit consentono di verificare il comportamento del tuo codice in modo automatizzato, garantendo che le varie componenti del tuo progetto funzionino correttamente.
- Crea una directory `src/test/java` nella struttura del tuo progetto Spring Boot se non presente, esempio ([test](src%2Ftest)). All'interno di questa directory, crea classi di test per testare le varie componenti del tuo codice.
- Puoi organizzare i tuoi test JUnit in classi separate, ognuna delle quali si concentra su una determinata parte del codice da testare e deve essere annotata con `@SpringBootTest`, per avviare l'intera applicazione Spring Boot durante l'esecuzione del test. All'interno di ogni classe di test, definisci vari metodi di test per verificare il comportamento delle singole funzionalità, esempio ([SpringbootGuideApplicationTests.java](src%2Ftest%2Fjava%2Feu%2Ftasgroup%2Fspringbootguide%2FSpringbootGuideApplicationTests.java)).
- Utilizza le asserzioni JUnit per verificare se i risultati ottenuti durante l'esecuzione dei test corrispondono a quelli attesi. Alcuni esempi di asserzioni includono assertEquals, assertTrue, assertNotNull, ecc.
***
### 3. Configurazione delle Properties
- Assicurati che le properties del tuo progetto nel test siano coerenti con quelle utilizzate nel main. Ciò è particolarmente importante se il test dipende dalle properties specificate nel main per la sua esecuzione corretta.