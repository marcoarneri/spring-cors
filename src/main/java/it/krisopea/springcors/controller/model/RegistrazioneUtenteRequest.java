package it.krisopea.springcors.controller.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.apache.avro.AvroRuntimeException;
import org.apache.avro.Schema;
import org.apache.avro.reflect.AvroEncode;
import org.apache.avro.reflect.AvroName;

@Data
public class RegistrazioneUtenteRequest extends org.apache.avro.specific.SpecificRecordBase
        implements org.apache.avro.specific.SpecificRecord {

    @NotBlank
    private String mail;

    @NotBlank
    private String password;

    @Override
    public Schema getSchema() {
        return Schema.createRecord("RegistrazioneUtenteRequest", null, null, false);
    }

    // Implementazione del metodo get(int i)
    @Override
    public Object get(int i) {
        switch (i) {
            case 0: return mail;
            case 1: return password;
            default: throw new AvroRuntimeException("Indice non valido: " + i);
        }
    }

    // Implementazione del metodo put(int i, Object o)
    @Override
    public void put(int i, Object o) {
        switch (i) {
            case 0: mail = (String) o; break;
            case 1: password = (String) o; break;
            default: throw new AvroRuntimeException("Indice non valido: " + i);
        }
    }

//    @Valid
//    @NotNull
//    private DettagliUtente dettagliUtente;

}
