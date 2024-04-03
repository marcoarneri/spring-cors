# Spring Batch

Spring Boot Batch è un framework di Spring che fornisce supporto per l'esecuzione di processi batch. È utile per eseguire attività pianificate, come l'elaborazione di grandi quantità di dati, l'importazione/esportazione di dati da e verso fonti esterne, e altre operazioni di elaborazione.

## Passaggi

Segui questi passaggi per aggiungere spring-batch al tuo progetto Spring Boot:

## Dipendenze

aggiungi la dipendenza maven per importare spring-batch

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-batch</artifactId>
    <version>2.7.16</version>
</dependency>
```

## Properties

aggiungi al file application.properties le seguenti properties per la configurazione del db

```properties
#Driver necessario per la gestione delle transazioni
spring.datasource.driver-class-name=org.postgresql.Driver
#Indica che lo schema del database utilizzato per Spring Batch verrà sempre inizializzato ad ogni avvio dell'applicazione
#always, never
spring.batch.jdbc.initialize-schema=always
```

## Funzionamento

### 1. File CSV

Come esempio di lettura di dati da una fonte esterna abbiamo creato un file [demo.csv](..%2F..%2Fsrc%2Fmain%2Fresources%2Fdemo.csv).

### 2. Creazione Job e Processi batch

- Crea i job e i processi batch necessari per la tua applicazione. Un job batch è un'istanza del tipo Job, mentre un processo batch è un'istanza del tipo Step.
- Implementa i lettori, i processori e i writer necessari per l'elaborazione dei dati. Un lettore batch legge i dati da una fonte esterna, un processore batch elabora i dati e un writer batch scrive i dati su una destinazione.
- Configura in base alle tue esigenze un `Listener`, che implementa la classe `JobExecutionListener` per intercettare quando viene avviato e quando finisce il Job.

esempi:
- [BatchConfiguration](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fbatchprocessing%2Fconfig%2FBatchConfiguration.java)
- [DemoItemProcessor](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fbatchprocessing%2FDemoItemProcessor.java)
- [DemoJobNotificationListener](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fbatchprocessing%2FDemoJobNotificationListener.java)

### 3. Riavvio di un job in caso di fallimento

La classe [JobRestarter.java](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fbatchprocessing%2FJobRestarter.java) è responsabile di gestire il riavvio di un job in caso di fallimento dell'esecuzione precedente.


Come prima cosa instanziamo due campi Importati per esplorare i metadati del job e fare delle operazioni su di essi

* `jobExplorer`:

  Questo campo rappresenta un'istanza di `JobExplorer`, che fornisce metodi per esplorare i metadati dei job. Viene utilizzato per ottenere informazioni sull'ultima esecuzione del job.


* `jobOperator`:

    Questo campo rappresenta un'istanza di `JobOperator`, che offre metodi per operare sui job, come avviare, fermare e riavviare i job. Nel caso del nostro esempio viene utilizzato per riavviare il nostro job in caso di fallimento.

Il metodo `restartJob()` fornisce un meccanismo robusto per gestire i fallimenti dei job. Controlla se l'ultima esecuzione del job sia fallita e, in caso affermativo, riavvia il job dalla posizione in cui si è interrotto.

In conclusione, il metodo restartJob() rappresenta un componente cruciale nell'architettura di un'applicazione Spring Batch, garantendo che i job vengano gestiti in modo robusto e affidabile anche in presenza di errori o fallimenti durante l'esecuzione.


### 4. Attivazione Job

Esegui il job batch utilizzando il `JobLauncher`. Puoi avviare i job batch manualmente tramite il codice Java o pianificare l'esecuzione dei job batch utilizzando strumenti come Spring Scheduler

esempio:
- [BatchScheduler](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fbatchprocessing%2Fconfig%2FBatchScheduler.java)
