package it.krisopea.springcors.kafka.config.avro;

import it.krisopea.springcors.kafka.model.UtenteAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class KafkaAvroProducer {

    @Autowired
    private KafkaTemplate<String, UtenteAvro> kafkaAvroTemplate;

    public void send(UtenteAvro request) {
        String key = "Key" + Math.random();
        CompletableFuture<SendResult<String, UtenteAvro>> future = kafkaAvroTemplate.send("kafka-avro-test", key,request);
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
