package it.krisopea.springcors.service.dto;

import lombok.Data;

@Data
public class VueClientResponseDto {

    private Long id;

    private String nome;

    private String cognome;

    private String username;

    private Long eta;
}
