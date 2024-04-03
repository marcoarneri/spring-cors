# Kafka

Apache Kafka è un sistema di elaborazione dei flussi distribuito e tollerante ai guasti.

## Panoramica

Ecco alcuni concetti chiave relativi a Kafka:

- Kafka si basa sul modello publish/subscribe. Questo significa che i dati vengono inviati ai "topic", che sono canali virtuali dove i dati vengono pubblicati dai produttori e consumati dai consumatori.
- **Broker**: Un "broker" in Kafka è un nodo del cluster Kafka che memorizza i dati. I broker sono responsabili della memorizzazione e della gestione dei dati.
- **Topic**: Un "topic" in Kafka è una categoria di flusso di dati a cui i produttori inviano messaggi e da cui i consumatori leggono messaggi. Ogni messaggio inviato a Kafka è associato a un topic.
- **Partizioni**: I "topic" possono essere suddivisi in "partizioni". Ogni partizione è una sequenza ordinata e immutabile di messaggi. Le partizioni consentono di parallelizzare il carico e distribuire i dati su più nodi nel cluster Kafka. Ogni messaggio in un topic ha un "offset" univoco, che rappresenta la posizione del messaggio all'interno della partizione.
- **Consumer Group**: I "consumer" di Kafka sono organizzati in "consumer group". Ogni consumer group legge i messaggi da uno o più topic. Ogni messaggio di un topic viene letto da uno e solo uno dei consumer all'interno del gruppo.

### Vantaggi

Le partizioni sono uno dei concetti più importanti e distintivi di Kafka. Servono a diversi scopi:

- **Scalabilità**: Consentono di distribuire il carico dei dati su più nodi del cluster Kafka. Più partizioni significano più nodi possono lavorare in parallelo per gestire il flusso di dati.

- **Tolleranza agli errori**: Ogni partizione di Kafka può essere replicata su più nodi, garantendo che i dati siano disponibili anche se uno dei nodi del cluster fallisce.

- **Ordine dei messaggi**: All'interno di una partizione, i messaggi sono ordinati per offset. Questo garantisce che i messaggi siano elaborati nell'ordine corretto.

## Passaggi

Segui questi passaggi per integrare kafka al tuo progetto Spring Boot:

### 1. Installazione e configurazione

Per scaricare e installare Kafka, fare riferimento alla guida ufficiale [QUI](https://kafka.apache.org/quickstart)

### 2. Aggiunta dipendenze e plugin

aggiungi le seguenti dipendenze e plugin al tuo file [pom.xml](..%2F..%2Fpom.xml)

```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
    <version>3.1.2</version>
</dependency>
```

```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
        <!-- fai riferimento al percorso della tua classe main -->
        <mainClass>com.baeldung.spring.kafka.KafkaApplication</mainClass>
    </configuration>
</plugin>
```

### 3. Configurazione Topic Kafka

Fai riferimento alla classe di esempio [KafkaTopicConfig](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fkafka%2Fconfig%2FKafkaTopicConfig.java)

- **kafkaAdmin**: Questo metodo crea e configura un bean KafkaAdmin. KafkaAdmin è una classe di utilità di Spring Kafka che facilita la creazione e l'amministrazione di topic di Kafka. Viene configurato con le proprietà specificate, come ad esempio gli indirizzi dei server di bootstrap.
- **topic1**: Questo metodo crea un bean NewTopic, che rappresenta un topic di Kafka. Viene creato un nuovo topic chiamato "kafkatest" con una sola partizione e un fattore di replica di 1. Il primo argomento specifica il nome del topic, il secondo specifica il numero di partizioni e il terzo specifica il fattore di replica.
- **fattore di replica**: Il fattore di replica in Kafka si riferisce al numero di copie dei messaggi di un particolare topic che vengono mantenute all'interno del cluster Kafka. Ad esempio, se un topic ha un fattore di replica di 3, significa che ogni messaggio nel topic sarà replicato in 3 copie all'interno del cluster Kafka. 

### 4. Configurazione Producer Kafka

Fai riferimento alla classe di esempio [KafkaProducerConfig](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fkafka%2Fconfig%2FKafkaProducerConfig.java)

Questa classe di configurazione in Spring Boot per Kafka si occupa di configurare e creare un ProducerFactory e un KafkaTemplate.

- **producerFactory**: Il metodo producerFactory() crea e restituisce un oggetto ProducerFactory che è responsabile della creazione di produttori Kafka. Vengono specificate le proprietà di configurazione necessarie per creare il produttore, come ad esempio gli indirizzi dei server (bootstrap.servers) e i serializzatori per le chiavi e i valori dei messaggi.
- **KafkaTemplate**: Il metodo kafkaTemplate() restituisce un oggetto KafkaTemplate, che è un'astrazione fornita da Spring Kafka per semplificare la produzione di messaggi in Kafka. Viene costruito utilizzando il ProducerFactory creato precedentemente.

### 5. Configurazione Consumer kafka

Fai riferimento alla classe di esempio [KafkaConsumerConfig](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fkafka%2Fconfig%2FKafkaConsumerConfig.java)

Questa classe di configurazione in Spring Boot per Kafka si occupa di configurare e creare un ConsumerFactory e un ConcurrentKafkaListenerContainerFactory.

- **consumerFactory**: Il metodo consumerFactory() crea e restituisce un oggetto ConsumerFactory che è responsabile della creazione di consumatori Kafka. Vengono specificate le proprietà di configurazione necessarie per creare il consumatore, come ad esempio gli indirizzi dei server (bootstrap.servers), l'ID del gruppo (group.id) e i deserializzatori per le chiavi e i valori dei messaggi.

- **kafkaListenerContainerFactory**: Il metodo kafkaListenerContainerFactory() restituisce un oggetto ConcurrentKafkaListenerContainerFactory, che è responsabile della creazione di container di ascolto per i consumatori Kafka. Viene impostata la factory del consumatore creato precedentemente come consumer factory per il container di ascolto.

### 6. Integrazione Producer/Consumer

fai riferimento alle classi di esempio [KafkaJsonProducer](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fkafka%2FKafkaJsonProducer.java) & [KafkaJsonConsumer](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fkafka%2FKafkaJsonConsumer.java).

Il producer viene poi iniettato all'interno del servizio [DemoService](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fservice%2FDemoService.java) e inviato un messaggio al topic "kafkatest", che verrà intercettato dal listener del consumer e loggato.