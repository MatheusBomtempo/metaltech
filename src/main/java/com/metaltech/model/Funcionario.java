package com.metaltech.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "funcionarios")
@EqualsAndHashCode(callSuper = true)
public class Funcionario extends Pessoa {
    @Enumerated(EnumType.STRING)
    private Cargo cargo;
    
    private BigDecimal salario;
    
    private LocalDate dataContratacao;
} 