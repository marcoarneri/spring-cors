# Basi Architettura

## Basi per creare il progetto

Questa guida ti aiuterà a creare un nuovo progetto Spring Boot con un setup di base utilizzando Spring Initializr.
***
## Passaggi

Segui questi passaggi per creare il tuo progetto Spring Boot:

1. **Accesso a Spring Initializr**: Apri il tuo browser web e visita [Spring Initializr](https://start.spring.io/).


2. **Configurazione del Progetto**:
    - **Sistema di Build**: Scegli tra Maven e Gradle come sistema di build.
    - **Linguaggio**: Seleziona la versione di Java per il tuo progetto.
    - **Versione di Spring Boot**: Scegli la versione desiderata di Spring Boot per il tuo progetto.
    - **Gruppo**: Questo campo rappresenta il package base del tuo progetto, ad esempio `com.example`.
    - **Artifact**: L'artifact ID identifica il nome del tuo progetto.
    - **Dipendenze**: Seleziona le dipendenze desiderate per il tuo progetto.


3. **Generazione del Progetto**:
    - Dopo aver configurato il tuo progetto, clicca sul pulsante "Generate" per generare il tuo progetto Spring Boot.


4. **Scarica il Progetto**:
    - Una volta generato, verrà scaricato un file ZIP contenente il tuo progetto Spring Boot.


5. **Importa il Progetto nel tuo IDE**:
    - Estrai il file ZIP scaricato e importa il progetto nel tuo IDE. Per IntelliJ IDEA, puoi aprire il progetto come progetto Gradle o Maven.


6. **Start Developing**:
    - Ora sei pronto per iniziare lo sviluppo del tuo progetto Spring Boot! Puoi iniziare a scrivere codice nelle classi generate e aggiungere ulteriori dipendenze secondo le tue esigenze.
	

## Architettura 

Per questo progetto utiliziamo una architettura MVC

- **Controller**: Nel contesto di Spring Boot, i controller sono responsabili di ricevere le richieste HTTP dai client. Un controller accetta le richieste, elabora i dati necessari e poi delega l'elaborazione dei dati a livelli inferiori dell'applicazione (Service). Il controller non dovrebbe contenere la logica di business, ma piuttosto agisce come un intermediario tra il client e il resto dell'applicazione.
- **Service**:  Il layer del service contiene la logica di business dell'applicazione. In questo strato, vengono implementate le operazioni che l'applicazione deve eseguire sui dati. Il service prende i dati forniti dal controller, li elabora secondo le regole di business dell'applicazione e li passa eventualmente alla repository per l'accesso ai dati.
- **Repository**:  Il layer della repository è responsabile dell'accesso ai dati persistenti, come un database. La repository interagisce direttamente con il database per recuperare o modificare i dati richiesti. In Spring Boot, le repository sono spesso implementate utilizzando il framework Spring Data JPA, che semplifica notevolmente lo sviluppo di operazioni di accesso ai dati.