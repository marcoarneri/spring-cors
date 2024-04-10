package it.krisopea.springcors.controller.model;

import lombok.Data;

@Data
public class RegistrazioneUtenteRequest {

    private String mail;
    private String password;

}
