package it.krisopea.springcors.service.dto;

import it.krisopea.springcors.repository.model.RoleEntity;
import lombok.Data;

import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

@Data
public class AngularClientResponseDto {

    private Long id;

    private String nome;

    private String cognome;

    private String username;

    private Long eta;
}
