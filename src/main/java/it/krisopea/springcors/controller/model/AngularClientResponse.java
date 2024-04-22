package it.krisopea.springcors.controller.model;

import it.krisopea.springcors.repository.model.RoleEntity;
import lombok.Data;

import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

@Data
public class AngularClientResponse {

    private Long id;

    private String nome;

    private String cognome;

    private String username;

    private Long eta;

}
