package it.krisopea.springcors.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.krisopea.springcors.service.dto.DemoRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    @Autowired
    private ObjectMapper objectMapper;


    @KafkaListener(topics = "kafkatest", groupId = "spring-cors")
    public void listenJsonKafkaTest(String jsonMessage) throws JsonProcessingException {
        DemoRequestDto requestDto = objectMapper.readValue(jsonMessage, DemoRequestDto.class);
        System.out.println("Messaggio ricevuto nel gruppo spring-cors: " + requestDto);
        // Puoi qui fare qualsiasi elaborazione desideri con l'oggetto DemoRequestDto ricevuto
    }

    @KafkaListener(
            topics = "kafkafiltertest",
            groupId = "spring-cors-filter",
            containerFactory = "filterKafkaListenerContainerFactory")
    public void listenWithFilter(String message) {
        System.out.println("Received Message in filtered listener: " + message);
    }

//    @KafkaListener(
//            topics = "kafkacustomtopic",
//            containerFactory = "customKafkaListenerContainerFactory")
//    public void customListener(DemoRequestDto demoRequestDto) {
//        System.out.println("Received custom Message in custom listener: " + demoRequestDto);
//    }

//    @KafkaListener(topics = "kafkatest", groupId = "spring-cors")
//    public void listenWithHeaders(
//            @Payload String jsonMessage,
//            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) throws JsonProcessingException {
//        DemoRequestDto requestDto = objectMapper.readValue(jsonMessage, DemoRequestDto.class);
//        System.out.println(
//                "Received Message: " + requestDto + "from partition: " + partition);
//    }

//    @KafkaListener(topicPartitions
//            = @TopicPartition(topic = "kafkatest", partitions = { "0", "1" }),
//    groupId = "spring-cors")
//    public void listenToPartition(
//            @Payload String jsonMessage,
//            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) throws JsonProcessingException {
//        DemoRequestDto requestDto = objectMapper.readValue(jsonMessage, DemoRequestDto.class);
//        System.out.println(
//                "Received Message: " + requestDto
//                        + "from partition: " + partition);
//    }
}
