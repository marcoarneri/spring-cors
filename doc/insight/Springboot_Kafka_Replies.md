## Passaggi

Segui questi passaggi per integrare la logica di replies a kafka:

### 1. creazione topic

- crea il topic replies all'interno della classe di configurazione [KafkaTopicConfig](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fkafka%2Fconfig%2FKafkaTopicConfig.java)

### 2. creazione ReplyingKafkaTemplate

- crea il bean `ReplyingKafkaTemplate` all'interno della classe di configurazione [KafkaReplyConfig](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fkafka%2Fconfig%2FKafkaReplyConfig.java)

### 3. creazione repliesContainer

- creai il bean `repliesContainer` all'interno della classe di configurazione [KafkaReplyConfig](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fkafka%2Fconfig%2FKafkaReplyConfig.java)

### 4. creazione repliesContainerFactory

- creai il bean `repliesContainerFactory` all'interno della classe di configurazione [KafkaReplyConfig](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fkafka%2Fconfig%2FKafkaReplyConfig.java)

### 5. implementa il servizio e utilizza  ReplyingKafkaTemplate

- crea il servizio [KafkaReplyService](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fservice%2FKafkaReplyService.java), utilizza il template e aspetta che venga consumato il messaggio

