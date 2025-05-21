package com.metaltech.dao;

import com.metaltech.model.Produto;
import java.util.List;

public class ProdutoDAO extends BaseDAO<Produto> {
    
    public ProdutoDAO() {
        super(Produto.class);
    }
    
    @SuppressWarnings("unchecked")
    public List<Produto> findByNome(String nome) {
        return em.createQuery("SELECT p FROM Produto p WHERE p.nome LIKE :nome")
                .setParameter("nome", "%" + nome + "%")
                .getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<Produto> findByFornecedor(Long fornecedorId) {
        return em.createQuery("SELECT p FROM Produto p WHERE p.fornecedor.id = :fornecedorId")
                .setParameter("fornecedorId", fornecedorId)
                .getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<Produto> findByPrecoRange(Double precoMin, Double precoMax) {
        return em.createQuery("SELECT p FROM Produto p WHERE p.preco BETWEEN :precoMin AND :precoMax")
                .setParameter("precoMin", precoMin)
                .setParameter("precoMax", precoMax)
                .getResultList();
    }
} 