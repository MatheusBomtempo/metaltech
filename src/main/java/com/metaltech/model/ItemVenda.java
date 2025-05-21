package com.metaltech.model;

import javax.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Data
@Entity
@Table(name = "itens_venda")
public class ItemVenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "venda_id", nullable = false)
    @JsonBackReference
    private Venda venda;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private BigDecimal precoUnitario;

    @Column(nullable = false)
    private BigDecimal subtotal;

    @PrePersist
    @PreUpdate
    public void calcularSubtotal() {
        this.subtotal = this.precoUnitario.multiply(BigDecimal.valueOf(this.quantidade));
        
        // Validar e atualizar estoque
        if (produto.getQuantidadeEstoque() < quantidade) {
            throw new IllegalArgumentException("Quantidade insuficiente em estoque");
        }
        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - quantidade);
    }
} 