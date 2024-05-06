package it.krisopea.springcors.controller.model;

import lombok.Data;

@Data
public class ReactClientResponse {

    private Long id;

    private String nome;

    private String cognome;

    private String username;

    private Long eta;

    private String imgUrl;

}
