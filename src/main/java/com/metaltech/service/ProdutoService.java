package com.metaltech.service;

import com.metaltech.model.Produto;
import com.metaltech.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProdutoService {
    
    @Autowired
    private ProdutoRepository repository;

    @Transactional
    public Produto salvar(Produto produto) {
        if (repository.existsByCodigo(produto.getCodigo())) {
            throw new IllegalArgumentException("Já existe um produto cadastrado com este código");
        }
        return repository.save(produto);
    }

    public Produto buscarPorId(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
    }

    public List<Produto> listarTodos() {
        return repository.findAll();
    }

    public List<Produto> buscarPorCategoria(String categoria) {
        return repository.findByCategoria(categoria);
    }

    public List<Produto> buscarProdutosBaixoEstoque() {
        return repository.findByQuantidadeEstoqueLessThan(10);
    }

    @Transactional
    public void atualizarEstoque(Long id, Integer quantidade) {
        Produto produto = buscarPorId(id);
        produto.setQuantidadeEstoque(quantidade);
        repository.save(produto);
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Produto não encontrado");
        }
        repository.deleteById(id);
    }
} 