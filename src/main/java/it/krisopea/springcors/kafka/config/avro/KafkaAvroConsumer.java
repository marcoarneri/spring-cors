package it.krisopea.springcors.kafka.config.avro;

import it.krisopea.springcors.controller.model.RegistrazioneUtenteRequest;
import org.apache.avro.generic.GenericRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaAvroConsumer {

    @KafkaListener(topics = "kafka-avro-test", groupId = "spring-cors")
    public void listen(GenericRecord message) {
        System.out.println("Received avro message: " + message);
    }
}
