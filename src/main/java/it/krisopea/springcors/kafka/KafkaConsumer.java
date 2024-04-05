package it.krisopea.springcors.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.net.SocketTimeoutException;

@Component
@Slf4j
public class KafkaConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "kafkatest", groupId = "spring-cors", containerFactory = "kafkaListenerContainerFactory")
    public void listenJsonKafkaTest(String jsonMessage) throws JsonProcessingException, SocketTimeoutException {
        throw new SocketTimeoutException("Simulated exception");
    }

    @KafkaListener(topics = "requests", groupId = "spring-cors")
    public void listenRequestReply(String message) {
        log.info("reply request received: " + message);
        kafkaTemplate.send("replies", message);
    }

    @KafkaListener(topics = "replies", groupId = "spring-cors")
    public void listenReply(String message) {
        log.info("reply response received: " + message);
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
