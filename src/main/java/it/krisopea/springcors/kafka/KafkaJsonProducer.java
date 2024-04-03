package it.krisopea.springcors.kafka;

import it.krisopea.springcors.service.dto.DemoRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.CompletableFuture;

@Component
public class KafkaJsonProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private ObjectMapper objectMapper;


    public void sendJsonMessage(DemoRequestDto requestDto) throws JsonProcessingException {
        String jsonMessage = objectMapper.writeValueAsString(requestDto);
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("kafkatest", jsonMessage);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + jsonMessage +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" +
                        jsonMessage + "] due to : " + ex.getMessage());
            }
        });
    }

    public void sendFilterMessage(String message) throws JsonProcessingException {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("kafkafiltertest", message);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + message +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" +
                        message + "] due to : " + ex.getMessage());
            }
        });
    }
}
