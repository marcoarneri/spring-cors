# Openapi

## Passaggi

Segui questi passaggi per aggiungere la parte di openapi al tuo progetto Spring Boot:

### 1. Aggiunta delle Dipendenze Necessarie

- Per iniziare, aggiungi le seguenti dipendenze al tuo file `pom.xml` per gestire la validazione dei dati e semplificare la creazione di classi modello:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.2.0</version>
</dependency>
```
***
### 2. Avvia l'applicazione

- avvia l'applicazione e raggiungi l'endpoint: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) per vedere lo swagger UI