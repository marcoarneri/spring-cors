package it.krisopea.springcors.controller.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AngularClientRequest {

    private Long id;

    @NotBlank
    @Size(min = 3, max = 50)
    private String nome;

    @NotBlank
    @Size(min = 3, max = 50)
    private String cognome;

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    private Long eta;

}
