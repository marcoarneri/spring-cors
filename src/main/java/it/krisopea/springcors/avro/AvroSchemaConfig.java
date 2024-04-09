package it.krisopea.springcors.avro;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.springframework.stereotype.Component;

@Component
public class AvroSchemaConfig {


    public Schema registrazioneUtenteRequestSchema = SchemaBuilder.record("RegistrazioneUtenteRequest")
            .namespace("it.krisopea.springcors.controller.model")
            .fields()
            .requiredString("mail")
            .requiredString("password")
            .endRecord();

}
