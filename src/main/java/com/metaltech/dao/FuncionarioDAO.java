package com.metaltech.dao;

import com.metaltech.model.Funcionario;
import java.util.List;

public class FuncionarioDAO extends BaseDAO<Funcionario> {
    
    public FuncionarioDAO() {
        super(Funcionario.class);
    }
    
    @SuppressWarnings("unchecked")
    public List<Funcionario> findByNome(String nome) {
        return em.createQuery("SELECT f FROM Funcionario f WHERE f.nome LIKE :nome")
                .setParameter("nome", "%" + nome + "%")
                .getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<Funcionario> findByCargo(String cargo) {
        return em.createQuery("SELECT f FROM Funcionario f WHERE f.cargo = :cargo")
                .setParameter("cargo", cargo)
                .getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<Funcionario> findByCpf(String cpf) {
        return em.createQuery("SELECT f FROM Funcionario f WHERE f.cpf = :cpf")
                .setParameter("cpf", cpf)
                .getResultList();
    }
} 