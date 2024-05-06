package it.krisopea.springcors.repository.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "VUE_CLIENT_ENTITY")
public class VueClientEntity {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOME", nullable = false)
    private String nome;

    @Column(name = "COGNOME", nullable = false)
    private String cognome;

    @Column(name = "USERNAME", unique = true, nullable = false)
    private String username;

    @Column(name = "ETA", nullable = false)
    private Long eta;
}
