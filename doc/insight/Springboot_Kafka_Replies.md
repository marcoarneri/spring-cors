## Passaggi

Segui questi passaggi per integrare la logica di replies a kafka:

### 1. creazione topic

- crea 2 topic requests e replies all'interno della classe di configurazione [KafkaTopicConfig](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fkafka%2Fconfig%2FKafkaTopicConfig.java)

### 2. creazione ReplyingKafkaTemplate

- crea il bean `ReplyingKafkaTemplate` all'interno della classe di configurazione [KafkaProducerConfig](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fkafka%2Fconfig%2FKafkaProducerConfig.java)

### 3. creazione replyFactory

- creai il bean `replyFactory` all'interno della classe di configurazione [KafkaConsumerConfig](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fkafka%2Fconfig%2FKafkaConsumerConfig.java)

### 4. utilizza ReplyingKafkaTemplate

- utilizza il metodo `.sendAndReceive` del tuo ReplyingKafkaTemplate per inviare la richiesta al topic requests che mandera il message al topic replies
- rifai la chiamata per vedere la risposta HTTP -> [DemoController](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fcontroller%2FDemoController.java)

