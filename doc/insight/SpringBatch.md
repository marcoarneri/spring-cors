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

#### Passaggio 1: Configurazione del contesto di Spring Batch

- Annota la classe di configurazione con `@Configuration` per indicare a Spring che questa classe è una classe di configurazione che definisce i bean per l'applicazione.
- Crea i job e gli step necessari per la tua applicazione. Un job batch è un'istanza del tipo Job, mentre uno step è un'istanza del tipo Step.

#### Passaggio 2: Configurazione dei lettori

- Implementa i `Reader`, i `Processor` e i `Writer` necessari per l'elaborazione dei dati. Un `Reader` batch legge i dati da una fonte esterna, un `Processor` batch elabora i dati e un `Writer` batch scrive i dati su una destinazione.
- Configura in base alle tue esigenze un `Listener`, che implementa la classe `JobExecutionListener` per intercettare quando viene avviato e quando finisce il Job.

#### Nota bene
Ognuno di questi componenti può cambiare a secondo delle necessita ad esempio per leggere da un file bisogna usare la classe `FlatFileItemReader` o per leggere più file la classe `MultiResourceItemReader`, ecc.
Quindi per la configurazione di questi componenti prima verificare a cosa servono per utilizzare l'implementazione corretta

esempi:
- [BatchConfiguration](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fbatchprocessing%2Fconfig%2FBatchConfiguration.java)
- [DemoItemProcessor](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fbatchprocessing%2FDemoItemProcessor.java)
- [DemoJobNotificationListener](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fbatchprocessing%2FDemoJobNotificationListener.java)


### 4. Attivazione Job

Esegui il job batch utilizzando il `JobLauncher`. Puoi avviare i job batch manualmente tramite il codice Java o pianificare l'esecuzione dei job batch utilizzando strumenti come Spring Scheduler

esempio:
- [BatchScheduler](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fbatchprocessing%2Fconfig%2FBatchScheduler.java)
