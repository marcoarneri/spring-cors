package it.krisopea.springcors.controller.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReactClientRequest {

    private Long id;

    @NotBlank(message = "{nome.notBlank}")
    @Size(min = 3, max = 50, message = "{nome.size}")
    private String nome;

    @NotBlank(message = "{cognome.notBlank}")
    @Size(min = 3, max = 50, message = "{cognome.size}")
    private String cognome;

    @NotBlank(message = "{username.notBlank}")
    @Size(min = 3, max = 50, message = "{username.size}")
    private String username;

    @NotNull(message = "{eta.notNull}")
    private Long eta;

    private String imgUrl;
}
