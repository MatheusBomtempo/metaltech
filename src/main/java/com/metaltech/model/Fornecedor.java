package com.metaltech.model;

import javax.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "fornecedores")
public class Fornecedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeEmpresa;

    @Column(nullable = false, unique = true)
    private String cnpj;

    @Column(nullable = false)
    private String telefone;

    @ElementCollection
    @CollectionTable(name = "fornecedor_materiais")
    @Column(name = "material")
    private List<String> tiposMaterial;
} 