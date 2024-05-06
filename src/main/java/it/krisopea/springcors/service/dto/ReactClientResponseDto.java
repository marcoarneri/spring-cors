package it.krisopea.springcors.service.dto;

import lombok.Data;

@Data
public class ReactClientResponseDto {

    private Long id;

    private String nome;

    private String cognome;

    private String username;

    private Long eta;

    private String imgUrl;
}
