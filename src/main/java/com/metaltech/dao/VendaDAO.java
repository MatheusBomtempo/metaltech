package com.metaltech.dao;

import com.metaltech.model.Venda;
import java.util.List;
import java.util.Date;

public class VendaDAO extends BaseDAO<Venda> {
    
    public VendaDAO() {
        super(Venda.class);
    }
    
    @SuppressWarnings("unchecked")
    public List<Venda> findByCliente(Long clienteId) {
        return em.createQuery("SELECT v FROM Venda v WHERE v.cliente.id = :clienteId")
                .setParameter("clienteId", clienteId)
                .getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<Venda> findByDataRange(Date dataInicio, Date dataFim) {
        return em.createQuery("SELECT v FROM Venda v WHERE v.data BETWEEN :dataInicio AND :dataFim")
                .setParameter("dataInicio", dataInicio)
                .setParameter("dataFim", dataFim)
                .getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<Venda> findByFormaPagamento(String formaPagamento) {
        return em.createQuery("SELECT v FROM Venda v WHERE v.formaPagamento = :formaPagamento")
                .setParameter("formaPagamento", formaPagamento)
                .getResultList();
    }
    
    public Double calcularTotalVendasPorPeriodo(Date dataInicio, Date dataFim) {
        return (Double) em.createQuery("SELECT SUM(v.valorTotal) FROM Venda v WHERE v.data BETWEEN :dataInicio AND :dataFim")
                .setParameter("dataInicio", dataInicio)
                .setParameter("dataFim", dataFim)
                .getSingleResult();
    }
} 