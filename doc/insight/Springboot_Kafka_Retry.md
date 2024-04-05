## Passaggi

Segui questi passaggi per integrare la logica di retry a kafka:

### 1. Creazione errorHandler

- crea un `@Bean` errorHandler nella classe di configurazione del tuo consumer (es. [KafkaConsumerConfig](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fkafka%2Fconfig%2FKafkaConsumerConfig.java))
- setta nell'errorHandler le eccezioni da mandare in retry e quelle che non vanno mandate in retry: `errorHandler.addRetryableExceptions(SocketTimeoutException.class);`
  `errorHandler.addNotRetryableExceptions(NullPointerException.class);`
- setta un intervallo di retry (ogni quanto deve riprovare) e un maxAttempts (quante volte massimo deve riprovare): `BackOff fixedBackOff = new FixedBackOff(interval, maxAttempts);`
- gestisci all'interno dell'errorHandler i casi di errore: `DefaultErrorHandler errorHandler = new DefaultErrorHandler((consumerRecord, exception) -> {
  log.error("NUMERO DI TENTATIVI CONSUMER ESAURITI!");
  }, fixedBackOff);`

### 2. Set errorHandler nel ConcurrentKafkaListenerContainerFactory

- setta l'errorHandler precedentemente creato all'interno del tuo bean `ConcurrentKafkaListenerContainerFactory`: `factory.setCommonErrorHandler(errorHandler());`

### 3. Creazione consumer di test

- crea un consumer di test che quando intercetta il message rilancia l'eccezione che vuoi mandare in retry:     `@KafkaListener(topics = "kafkatest", groupId = "spring-cors", containerFactory = "kafkaListenerContainerFactory")
  public void listenJsonKafkaTest(String jsonMessage) throws JsonProcessingException, SocketTimeoutException {
  throw new SocketTimeoutException("Simulated exception");
  }`