package com.metaltech.dao;

import com.metaltech.model.Fornecedor;
import java.util.List;

public class FornecedorDAO extends BaseDAO<Fornecedor> {
    
    public FornecedorDAO() {
        super(Fornecedor.class);
    }
    
    @SuppressWarnings("unchecked")
    public List<Fornecedor> findByNome(String nome) {
        return em.createQuery("SELECT f FROM Fornecedor f WHERE f.nome LIKE :nome")
                .setParameter("nome", "%" + nome + "%")
                .getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<Fornecedor> findByCnpj(String cnpj) {
        return em.createQuery("SELECT f FROM Fornecedor f WHERE f.cnpj = :cnpj")
                .setParameter("cnpj", cnpj)
                .getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<Fornecedor> findByCidade(String cidade) {
        return em.createQuery("SELECT f FROM Fornecedor f WHERE f.cidade = :cidade")
                .setParameter("cidade", cidade)
                .getResultList();
    }
} 