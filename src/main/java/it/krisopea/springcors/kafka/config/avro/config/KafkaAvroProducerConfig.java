package it.krisopea.springcors.kafka.config.avro.config;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import it.krisopea.springcors.avro.AvroSchemaConfig;
import it.krisopea.springcors.avro.AvroSchemaFileWriter;
import it.krisopea.springcors.controller.model.RegistrazioneUtenteRequest;
import org.apache.avro.Schema;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaAvroProducerConfig {

    @Value(value = "${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapAddress;

    @Value(value = "${spring.kafka.properties.schema.registry.url}")
    private String schemaRegistryUrl;

    @Autowired
    private AvroSchemaFileWriter avroSchemaFileWriter;

    @Autowired
    private AvroSchemaConfig avroSchemaConfig;

    @Bean
    public ProducerFactory<String, RegistrazioneUtenteRequest> customProducerFactory() throws IOException {
        Schema registrazioneUtenteSchema = avroSchemaConfig.registrazioneUtenteRequestSchema;
        String filePath = avroSchemaFileWriter.writeSchemaToFile(registrazioneUtenteSchema);
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        configProps.put("schema.registry.url", schemaRegistryUrl);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, RegistrazioneUtenteRequest> kafkaAvroTemplate() throws IOException {
        return new KafkaTemplate<>(customProducerFactory());
    }
}
