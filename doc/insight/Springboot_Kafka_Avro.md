## Panoramica

- Avro viene spesso utilizzato con Kafka per serializzare e deserializzare i dati trasmessi attraverso i topic di Kafka. Quando i dati vengono prodotti in un topic Kafka, vengono serializzati utilizzando Avro prima di essere inviati al broker Kafka. Allo stesso modo, quando i dati vengono consumati da un topic Kafka, vengono deserializzati utilizzando Avro per essere compresi e elaborati.
- Inoltre, Avro supporta l'evoluzione dello schema dei dati, il che significa che è possibile modificare lo schema dei dati senza interrompere la compatibilità con le versioni precedenti dei dati, garantendo una maggiore flessibilità e scalabilità nell'elaborazione dei dati in Kafka.

## Passaggi

Segui questi passaggi per integrare avro con kafka al tuo progetto Spring Boot:

### 0. Docker

Crea il file [docker-compose.yaml](..%2F..%2Fdocker-compose.yaml) e configura i servizi zookeper, kafka e schema-registry

### 1. Dipendenze e plugin

Aggiungi le seguenti dipendenze e plugin per integrare avro:

```xml
<!--  Questa dipendenza fornisce il compilatore Avro, che è necessario per compilare gli schemi Avro in file di classe Java. -->
<dependency>
    <groupId>org.apache.avro</groupId>
    <artifactId>avro-compiler</artifactId>
    <version>1.11.3</version>
</dependency>
<dependency>
<!-- Questo plugin Maven è utilizzato per generare automaticamente classi Java dai file di schema Avro. -->
<groupId>org.apache.avro</groupId>
<artifactId>avro-maven-plugin</artifactId>
<version>1.11.3</version>
</dependency>
<!-- Questa dipendenza fornisce il serializer Avro per Kafka.  -->
<dependency>
<groupId>io.confluent</groupId>
<artifactId>kafka-avro-serializer</artifactId>
<version>7.6.0</version>
</dependency>
```

```xml
<plugin>
    <groupId>org.apache.avro</groupId>
    <artifactId>avro-maven-plugin</artifactId>
    <version>1.8.2</version>
    <executions>
        <execution>
            <id>schemas</id>
            <phase>generate-sources</phase>
            <goals>
                <goal>schema</goal>
                <goal>protocol</goal>
                <goal>idl-protocol</goal>
            </goals>
            <configuration>
                <sourceDirectory>${project.basedir}/src/main/resources/avro</sourceDirectory>
                <outputDirectory>${project.build.directory}/generated-sources</outputDirectory>
            </configuration>
        </execution>
    </executions>
</plugin>
```

### 2. Creazione modello da de/serializzare

il plugin genererà le classi Avro a partire dagli schema al percorso specificato come source: `${project.basedir}/src/main/resources/avro`
nel percorso specificato come target: `${project.build.directory}/generated-sources`

### 3. Configurazione Producer

Crea una classe di configurazione come [KafkaAvroProducerConfig](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fkafka%2Fconfig%2Favro%2Fconfig%2FKafkaAvroProducerConfig.java)

E crea i seguenti bean:

- `ProducerFactory` per integrare avro aggiungi le proprietà:
  ```java
  configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
  configProps.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
  ```
- `KafkaTemplate`

### 3. Configurazione Consumer

Crea una classe di configurazione come [KafkaAvroConsumerConfig](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fkafka%2Fconfig%2Favro%2Fconfig%2FKafkaAvroConsumerConfig.java)

E crea i seguenti bean:

- `ConsumerFactory` per integrare avro aggiungi le proprietà:
 ```java
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
    props.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
```
- `ConcurrentKafkaListenerContainerFactory`

### 4. Implementazione Producer

Implementa il producer [KafkaAvroProducer](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fkafka%2Fconfig%2Favro%2FKafkaAvroProducer.java)

### 5. Implementazione Consumer

Implementa il consumer [KafkaAvroConsumer](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fkafka%2Fconfig%2Favro%2FKafkaAvroConsumer.java)

## Test

Fare riferimento alla classe [UtenteController](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fcontroller%2FUtenteController.java)

### 1. Utilizza il producer per mandare al topic il message

`kafkaAvroProducer.send(request);`

### 2. Utilizza il listener del consumer per consumare il record

Il listener del consumer intercetterà il/i record in coda e li consumerà

## Offset Explorer UI

Offset Explorer è uno strumento che fornisce un'interfaccia grafica per esplorare i topic, gli offset dei consumatori e i relativi commit in un cluster Kafka.

### 1. Installazione

Installa Offset Explorer sulla tua macchina all'indirizzo https://www.kafkatool.com/download.html e selezione il tuo sistema operativo (nel mio caso Windows 64-bit)

### 2. Connettiti

Connetti Offset Explorer a kafka e schema-registry.

- Avvia il tuo docker con il comando: `docker-compose up`
- Avvia il tuo applicativo

Apri Offset Explorer e segui i seguenti passaggi:



