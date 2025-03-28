package com.metaltech.repository;

import com.metaltech.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    boolean existsByCodigo(String codigo);
    List<Produto> findByCategoria(String categoria);
    List<Produto> findByQuantidadeEstoqueLessThan(Integer quantidade);
} 