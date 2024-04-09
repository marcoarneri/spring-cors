package it.krisopea.springcors.kafka.config.avro.config;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import it.krisopea.springcors.avro.AvroSchemaConfig;
import it.krisopea.springcors.avro.AvroSchemaFileWriter;
import it.krisopea.springcors.controller.model.RegistrazioneUtenteRequest;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaAvroConsumerConfig {

    @Value(value = "${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapAddress;

    @Value(value = "${spring.kafka.consumer.custom.group-id}")
    private String groupId;

    @Value(value = "${spring.kafka.properties.schema.registry.url}")
    private String schemaRegistryUrl;

    @Autowired
    private AvroSchemaFileWriter avroSchemaFileWriter;

    @Autowired
    private AvroSchemaConfig avroSchemaConfig;

    @Bean
    public ConsumerFactory<String, KafkaAvroDeserializer> customConsumerFactory() throws IOException {
//        Schema registrazioneUtenteSchema = avroSchemaConfig.registrazioneUtenteRequestSchema;
//        File schemaFile = avroSchemaFileWriter.writeSchemaToFile(registrazioneUtenteSchema);
        Map<String, Object> props = new HashMap<>();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                groupId);
        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        props.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        props.put(KafkaAvroDeserializerConfig.AUTO_REGISTER_SCHEMAS, true);
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KafkaAvroDeserializer>
    customKafkaListenerContainerFactory() throws IOException {

        ConcurrentKafkaListenerContainerFactory<String, KafkaAvroDeserializer> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(customConsumerFactory());
        return factory;
    }
}
