package com.metaltech.repository;

import com.metaltech.model.ItemVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {
    boolean existsByProdutoId(Long produtoId);
} 