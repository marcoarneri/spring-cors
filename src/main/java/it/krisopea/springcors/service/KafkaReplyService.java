package it.krisopea.springcors.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class KafkaReplyService {
    @Autowired
    private ReplyingKafkaTemplate<String, Object, Object> template;

    public Object kafkaRequestReply(Object request) throws Exception {
        ProducerRecord<String, Object> record = new ProducerRecord<>("replies", request);
        RequestReplyFuture<String, Object, Object> replyFuture = template.sendAndReceive(record);
        ConsumerRecord<String, Object> consumerRecord = replyFuture.get(60, TimeUnit.SECONDS);
        return consumerRecord.value();
    }
}