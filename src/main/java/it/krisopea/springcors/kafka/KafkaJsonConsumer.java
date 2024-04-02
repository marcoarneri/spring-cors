package it.krisopea.springcors.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.krisopea.springcors.service.dto.DemoRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaJsonConsumer {

    @Autowired
    private ObjectMapper objectMapper;


    @KafkaListener(topics = "kafkatest", groupId = "spring-cors")
    public void listenJsonKafkaTest(String jsonMessage) throws JsonProcessingException {
        DemoRequestDto requestDto = objectMapper.readValue(jsonMessage, DemoRequestDto.class);
        System.out.println("Messaggio ricevuto nel gruppo spring-cors: " + requestDto);
        // Puoi qui fare qualsiasi elaborazione desideri con l'oggetto DemoRequestDto ricevuto
    }

//    @KafkaListener(topics = "kafkatest", groupId = "spring-cors")
//    public void listenWithHeaders(
//            @Payload String jsonMessage,
//            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) throws JsonProcessingException {
//        DemoRequestDto requestDto = objectMapper.readValue(jsonMessage, DemoRequestDto.class);
//        System.out.println(
//                "Received Message: " + requestDto + "from partition: " + partition);
//    }

//    @KafkaListener(topicPartitions
//            = @TopicPartition(topic = "kafkatest", partitions = { "0", "1" }))
//    public void listenToPartition(
//            @Payload String message,
//            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
//        System.out.println(
//                "Received Message: " + message
//                        + "from partition: " + partition);
//    }
}
