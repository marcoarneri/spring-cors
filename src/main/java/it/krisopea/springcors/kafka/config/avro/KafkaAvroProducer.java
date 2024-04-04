package it.krisopea.springcors.kafka.config.avro;

import it.krisopea.springcors.controller.model.RegistrazioneUtenteRequest;
import org.apache.avro.generic.GenericRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class KafkaAvroProducer {

    @Autowired
    private KafkaTemplate<String, GenericRecord> kafkaAvroTemplate;

    public void send(GenericRecord request) {

        CompletableFuture<SendResult<String, GenericRecord>> future = kafkaAvroTemplate.send("kafka-avro-test", request);;
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + request +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" +
                        request + "] due to : " + ex.getMessage());
            }
        });
    }
}
