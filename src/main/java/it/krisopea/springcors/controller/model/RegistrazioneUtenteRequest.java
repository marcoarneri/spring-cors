package it.krisopea.springcors.controller.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegistrazioneUtenteRequest {

    @NotBlank
    private String mail;

    @NotBlank
    private String password;

    @Valid
    @NotNull
    private DettagliUtente dettagliUtente;

}
