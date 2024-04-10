package it.krisopea.springcors.kafka.config.avro;

import it.krisopea.springcors.kafka.model.UtenteAvro;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaAvroConsumer {

    @KafkaListener(topics = "kafka-avro-test", groupId = "spring-cors", containerFactory = "customKafkaListenerContainerFactory")
    public void listen(ConsumerRecord<String, UtenteAvro> consumerRecord) {
        System.out.println("Received avro message: " + consumerRecord.value());
    }

}
