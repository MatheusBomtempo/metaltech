package com.metaltech.model;

import javax.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@MappedSuperclass
public abstract class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String documentoFiscal;

    @Column(nullable = false)
    private String telefone;

    @Column(nullable = false)
    private LocalDate dataCadastro;

    @PrePersist
    protected void onCreate() {
        dataCadastro = LocalDate.now();
    }
} 