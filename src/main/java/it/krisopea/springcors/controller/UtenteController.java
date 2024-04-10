package it.krisopea.springcors.controller;

import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import it.krisopea.springcors.controller.model.RegistrazioneUtenteRequest;
import it.krisopea.springcors.kafka.config.avro.KafkaAvroProducer;
import it.krisopea.springcors.kafka.model.UtenteAvro;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UtenteController {

    @Autowired
    private KafkaAvroProducer kafkaAvroProducer;


    @PostMapping(
            value = "/registraUtente",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> demo(
            @Valid @RequestBody RegistrazioneUtenteRequest request) throws IOException, RestClientException {

        //FACCIO TUTTO NEL CONTROLLER PER VELOCIZZARE I TEMPI

        UtenteAvro utenteAvro = UtenteAvro.newBuilder()
                .setMail(request.getMail())
                .setPassword(request.getPassword())
                .build();

        kafkaAvroProducer.send(utenteAvro);

        return ResponseEntity.accepted().build();
    }

}
