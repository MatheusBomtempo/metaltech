package com.metaltech.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "clientes")
@EqualsAndHashCode(callSuper = true)
public class Cliente extends Pessoa {
    private String endereco;
} 