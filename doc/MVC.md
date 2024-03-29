# MVC

## Passaggi

Segui questi passaggi per aggiungere la parte MVC al tuo progetto Spring Boot:

### 1. Aggiunta delle Dipendenze Necessarie

- Per iniziare, aggiungi le seguenti dipendenze al tuo file `pom.xml` per gestire la validazione dei dati e semplificare la creazione di classi modello (ricorda di sostituire la dipendenza starter con starter-web):

```xml
<dependencys>
    <!--includendo la dipendenza spring-boot-starter-web stai avviando lo sviluppo di un'applicazione web basata su Spring Boot
     abilitando tutte le funzionalità necessarie per gestire le richieste HTTP, le risposte e l'esecuzione dell'applicazione web
      in un server incorporato.-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!--VALIDATION-->
    <!--Fornisce supporto per la validazione dei dati-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    
    <!--LOMBOK-->
    <!--Semplifica la creazione di classi modello aggiungendo automaticamente getter, setter e altri metodi-->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencys>
```
***
### 2. Creazione del Controller
Il controller gestisce le richieste HTTP degli utenti e coordina l'interazione tra il modello e la vista.

- Crea una nuova classe per il controller, ad esempio [DemoController](..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fcontroller%2FDemoController.java).
- Aggiungi le annotazioni al controller: `@RestController` (per indicare a springboot una classe come controller REST), `@RequestMapping` (per indicare il mapping di base per le chiamate REST), `@Validated` (Per attivare le validazioni sulla richiesta in entrata al controller), `@RequiredArgsConstructor` (utilizzato per l'injection di bean come il servizio all'interno del controller)
- All'interno del controller, definisci i metodi per gestire le diverse richieste HTTP, come `@GetMapping`, `@PostMapping`, ecc.
- Definisci i modelli di richiesta [DemoRequest](..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fcontroller%2Fmodel%2FDemoRequest.java) e di risposta [DemoResponse.java](..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fcontroller%2Fmodel%2FDemoResponse.java) utilizzati nei metodi del controller per mappare i dati inviati dall'utente e le risposte restituite al client.
***

### Lombok
Lombok permette di utilizzare annotazioni per generare automaticamente codice come getter, setter, toString, costruttori...

Alcuni esempi di annotazioni sono:
- `@Getter`
- `@Setter`
- `@Data`
- `@ToString`
- `@AllArgsConstructor`
- `@NoArgsConstructo`

### Validation
I validator permettono di utilizzare annotazioni per la gestione delle validazioni dei campi di request che arrivano il controller.

Alcuni esempi di annotazioni sono:
- `@NotBlank`
- `@Size`
- `@Pattern`
- `@NotNull`
- `@Valid`