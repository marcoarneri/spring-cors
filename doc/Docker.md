# Docker

### Attenzione!
Assicurati di avere Docker installato sul tuo sistema. Puoi scaricare e installare Docker dal sito ufficiale: [Docker Desktop](https://www.docker.com/products/docker-desktop/).
***

## Passaggi

Segui questi passaggi per integrare Docker nel tuo progetto Spring Boot:


### 1. Aggiunta delle Dipendenze Necessarie

- Per iniziare, aggiungi la seguente dipendenza al tuo file `pom.xml` per integrare PostgresSQL nel tuo progetto:

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```
***
### 2. Aggiunta del File docker-compose

- Crea un file `docker-compose.yml` nella root del tuo progetto Spring Boot, esempio ([docker-compose.yaml](docker-compose.yaml))
- Aggiungi la configurazione per il servizio del database PostgreSQL:

```yaml
version: '3.8'
services:
  postgres-db:
    image: 'postgres:latest'
    environment:
      - 'POSTGRES_DB=mydatabase'
      - 'POSTGRES_USER=postgres'
      - 'POSTGRES_PASSWORD=password'
    ports:
      - '5433:5432'
```
Questo file definisce un servizio Docker per un database PostgreSQL con il nome "postgres-db". Assicurati di aggiornare il nome del database, l'utente e la password secondo le tue esigenze.
***
### 3. Aggiunta delle Properties per il Collegamento al Database

- Aggiungi le seguenti properties nel file `application.properties` del tuo progetto Spring Boot per collegare l'applicazione al database PostgreSQL:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/mydatabase
spring.datasource.username=postgres
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=create
```
Sostituisci `mydatabase`, `postgres` e `password` con i valori corrispondenti specificati nel file docker-compose.yml.
***
### 4. Esecuzione dell'Applicazione con Docker

- Per eseguire l'applicazione Spring Boot insieme al database PostgreSQL utilizzando Docker, apri il terminale nella root del tuo progetto e esegui il comando:

```shell
docker-compose up
```
Questo comando avvierà entrambi i servizi definiti nel file docker-compose.yml.
