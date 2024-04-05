package it.krisopea.springcors.kafka.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value(value = "${spring.kafka.admin.properties.bootstrap.servers}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topic1() {
        return new NewTopic("kafkatest", 1, (short) 1);
    }

    @Bean
    public NewTopic topic2() {
        return new NewTopic("kafkafiltertest", 1, (short) 1);
    }

    @Bean
    public NewTopic topic3() {
        return new NewTopic("kafkacustomtopic", 1, (short) 1);
    }

    @Bean
    NewTopic topic4() {
        return TopicBuilder.name("replies").partitions(1).replicas(1).build();
    }

    @Bean
    NewTopic topic5() {
        return TopicBuilder.name("requests").partitions(1).replicas(1).build();
    }
}
