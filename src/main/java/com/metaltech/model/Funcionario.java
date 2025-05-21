package com.metaltech.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
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