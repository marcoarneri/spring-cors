package it.krisopea.springcors.repository.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "VUE_DATA_ENTITY")
public class VueDataEntity {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "MONTH", nullable = false)
    private String month;

    @Column(name = "AMOUNT", nullable = false)
    private Long amount;
}
