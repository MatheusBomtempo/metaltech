package com.metaltech.dao;

import com.metaltech.model.Cliente;
import java.util.List;

public class ClienteDAO extends BaseDAO<Cliente> {
    
    public ClienteDAO() {
        super(Cliente.class);
    }
    
    @SuppressWarnings("unchecked")
    public List<Cliente> findByNome(String nome) {
        return em.createQuery("SELECT c FROM Cliente c WHERE c.nome LIKE :nome")
                .setParameter("nome", "%" + nome + "%")
                .getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<Cliente> findByCpf(String cpf) {
        return em.createQuery("SELECT c FROM Cliente c WHERE c.cpf = :cpf")
                .setParameter("cpf", cpf)
                .getResultList();
    }
} 