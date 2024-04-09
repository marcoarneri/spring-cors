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
                <sourceDirectory>${project.basedir}/src/main/resources/</sourceDirectory>
                <outputDirectory>${project.basedir}/src/main/java/</outputDirectory>
            </configuration>
        </execution>
    </executions>
</plugin>
```

### 2. Creazione modello da de/serializzare

Crea un modello per avro e aggiungi le annotazioni:
- `@AvroGenerated` a livello di classe 
- `@AvroName("nomeCampo")` a livello di campo

```java
    import org.apache.avro.reflect.AvroName;
import org.apache.avro.specific.AvroGenerated;

@AvroGenerated
public class RegistrazioneUtenteRequest {

    @AvroName("mail")
    private String mail;
    
    @AvroName("password")
    private String password;
}
```

il plugin avro genererà una classe come la seguente: [RegistrazioneUtenteRequest](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fcontroller%2Fmodel%2FRegistrazioneUtenteRequest.java)

### 3. Configurazione Producer

Crea una classe di configurazione come [KafkaAvroProducerConfig](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fkafka%2Fconfig%2Favro%2Fconfig%2FKafkaAvroProducerConfig.java)

E crea i seguenti bean:

- `SchemaRegistryClient`
- `ProducerFactory` per integrare avro aggiungi le proprietà:
  `configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
  configProps.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);`
- `KafkaTemplate`

### 3. Configurazione Consumer

Crea una classe di configurazione come [KafkaAvroConsumerConfig](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fkafka%2Fconfig%2Favro%2Fconfig%2FKafkaAvroConsumerConfig.java)

E crea i seguenti bean:

- `ConsumerFactory` per integrare avro aggiungi le proprietà:
 ` props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
  props.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
  props.put(KafkaAvroDeserializerConfig.AUTO_REGISTER_SCHEMAS, true);
  props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);`
- `ConcurrentKafkaListenerContainerFactory`

### 4. Implementazione Producer

Implementa il producer [KafkaAvroProducer](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fkafka%2Fconfig%2Favro%2FKafkaAvroProducer.java)

### 5. Implementazione Consumer

Implementa il consumer [KafkaAvroConsumer](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fkafka%2Fconfig%2Favro%2FKafkaAvroConsumer.java)

## Test

Fare riferimento alla classe [UtenteController](..%2F..%2Fsrc%2Fmain%2Fjava%2Fit%2Fkrisopea%2Fspringcors%2Fcontroller%2FUtenteController.java)

### 1. Creazione di uno schema rappresentante il tuo model

Crea il tuo schema Schema `registrazioneUtenteSchema = avroSchemaConfig.registrazioneUtenteRequestSchema;`

### 2. Scrivi il tuo schema su file

Scrivi lo schema precedentemente creato su file .avsc `avroSchemaFileWriter.writeSchemaToFile(registrazioneUtenteSchema);`

### 3. Registra schema nello schema-registry

`ParsedSchema parsedSchema = new AvroSchema(registrazioneUtenteSchema);
schemaRegistryClient.register("RegistrazioneUtenteRequest", parsedSchema);`

### 4. Controlla che lo schema sia registrato

Controlla che lo schema sia effettivamente registrato:

`int schemaId = schemaRegistryClient.getId("RegistrazioneUtenteRequest", parsedSchema);
if (schemaId != -1) {
log.info("Lo schema {} è già registrato con ID {}", "RegistrazioneUtenteRequest", schemaId);
} else {
log.error("Lo schema {} non è stato registrato nello schema registry", "RegistrazioneUtenteRequest");
}`

Se il valore di schemaId è `-1` allora lo schema non è stato registrato correttamente

### 5. Utilizza il producer per mandare al topic il message

`kafkaAvroProducer.send(request);`

