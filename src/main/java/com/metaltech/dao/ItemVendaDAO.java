package com.metaltech.dao;

import com.metaltech.model.ItemVenda;
import java.util.List;

public class ItemVendaDAO extends BaseDAO<ItemVenda> {
    
    public ItemVendaDAO() {
        super(ItemVenda.class);
    }
    
    @SuppressWarnings("unchecked")
    public List<ItemVenda> findByVenda(Long vendaId) {
        return em.createQuery("SELECT i FROM ItemVenda i WHERE i.venda.id = :vendaId")
                .setParameter("vendaId", vendaId)
                .getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<ItemVenda> findByProduto(Long produtoId) {
        return em.createQuery("SELECT i FROM ItemVenda i WHERE i.produto.id = :produtoId")
                .setParameter("produtoId", produtoId)
                .getResultList();
    }
    
    public Double calcularTotalItensPorVenda(Long vendaId) {
        return (Double) em.createQuery("SELECT SUM(i.subtotal) FROM ItemVenda i WHERE i.venda.id = :vendaId")
                .setParameter("vendaId", vendaId)
                .getSingleResult();
    }
} 