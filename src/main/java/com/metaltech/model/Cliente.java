package com.metaltech.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "clientes")
@EqualsAndHashCode(callSuper = true)
public class Cliente extends Pessoa {
    private String endereco;
} 