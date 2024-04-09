package it.krisopea.springcors.controller;

import io.confluent.kafka.schemaregistry.ParsedSchema;
import io.confluent.kafka.schemaregistry.avro.AvroSchema;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import it.krisopea.springcors.avro.AvroSchemaConfig;
import it.krisopea.springcors.avro.AvroSchemaFileWriter;
import it.krisopea.springcors.controller.model.DemoRequest;
import it.krisopea.springcors.controller.model.DemoResponse;
import it.krisopea.springcors.controller.model.RegistrazioneUtenteRequest;
import it.krisopea.springcors.kafka.config.avro.KafkaAvroProducer;
import it.krisopea.springcors.service.dto.DemoRequestDto;
import it.krisopea.springcors.service.dto.DemoResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.springframework.beans.factory.annotation.Autowired;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UtenteController {

    @Autowired
    private KafkaAvroProducer kafkaAvroProducer;

    @Autowired
    private AvroSchemaFileWriter avroSchemaFileWriter;

    @Autowired
    private AvroSchemaConfig avroSchemaConfig;

    @Autowired
    private SchemaRegistryClient schemaRegistryClient;


    @PostMapping(
            value = "/registraUtente",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> demo(
            @Valid @RequestBody RegistrazioneUtenteRequest request) throws IOException, RestClientException {

        //FACCIO TUTTO NEL CONTROLLER PER VELOCIZZARE I TEMPI

        Schema registrazioneUtenteSchema = avroSchemaConfig.registrazioneUtenteRequestSchema;
        avroSchemaFileWriter.writeSchemaToFile(registrazioneUtenteSchema);

        ParsedSchema parsedSchema = new AvroSchema(registrazioneUtenteSchema);
        schemaRegistryClient.register("RegistrazioneUtenteRequest", parsedSchema);

        int schemaId = schemaRegistryClient.getId("RegistrazioneUtenteRequest", parsedSchema);
        if (schemaId != -1) {
            log.info("Lo schema {} è già registrato con ID {}", "RegistrazioneUtenteRequest", schemaId);
        } else {
            log.error("Lo schema {} non è stato registrato nello schema registry", "RegistrazioneUtenteRequest");
        }

        kafkaAvroProducer.send(request);

        return ResponseEntity.accepted().build();
    }

}
