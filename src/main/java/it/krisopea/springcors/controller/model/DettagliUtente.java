package it.krisopea.springcors.controller.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DettagliUtente {

    @NotBlank
    private String citta;

    @NotBlank
    private String stato;

}
