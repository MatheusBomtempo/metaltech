package com.metaltech.service;

import com.metaltech.model.Produto;
import com.metaltech.repository.ProdutoRepository;
import com.metaltech.repository.ItemVendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProdutoService {
    
    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private ItemVendaRepository itemVendaRepository;

    @Transactional
    public Produto salvar(Produto produto) {
        if (repository.existsByCodigo(produto.getCodigo())) {
            throw new IllegalArgumentException("Já existe um produto cadastrado com este código");
        }
        return repository.save(produto);
    }

    @Transactional
    public Produto atualizar(Produto produto) {
        Produto produtoExistente = buscarPorId(produto.getId());
        
        // Verifica se o código está sendo alterado e se já existe
        if (!produtoExistente.getCodigo().equals(produto.getCodigo()) && 
            repository.existsByCodigo(produto.getCodigo())) {
            throw new IllegalArgumentException("Já existe um produto cadastrado com este código");
        }

        produtoExistente.setCodigo(produto.getCodigo());
        produtoExistente.setNome(produto.getNome());
        produtoExistente.setPreco(produto.getPreco());
        produtoExistente.setQuantidadeEstoque(produto.getQuantidadeEstoque());
        produtoExistente.setCategoria(produto.getCategoria());

        return repository.save(produtoExistente);
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

        // Verifica se o produto está vinculado a alguma venda
        if (itemVendaRepository.existsByProdutoId(id)) {
            throw new IllegalArgumentException("Não é possível excluir um produto que está vinculado a vendas");
        }

        repository.deleteById(id);
    }
} 