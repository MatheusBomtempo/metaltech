package com.metaltech.service;

import com.metaltech.model.Venda;
import com.metaltech.model.ItemVenda;
import com.metaltech.model.Produto;
import com.metaltech.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VendaService {
    
    @Autowired
    private VendaRepository repository;

    @Autowired
    private ProdutoService produtoService;

    @Transactional
    public Venda salvar(Venda venda) {
        // Validar se o funcionário existe e é um representante de vendas
        if (venda.getFuncionario() == null) {
            throw new IllegalArgumentException("Funcionário é obrigatório para a venda");
        }

        // Validar se há itens na venda
        if (venda.getItens() == null || venda.getItens().isEmpty()) {
            throw new IllegalArgumentException("A venda deve conter pelo menos um item");
        }

        // Validar estoque e atualizar preços unitários
        for (ItemVenda item : venda.getItens()) {
            Produto produto = produtoService.buscarPorId(item.getProduto().getId());
            item.setProduto(produto);
            item.setPrecoUnitario(produto.getPreco());
            item.setVenda(venda);
        }

        return repository.save(venda);
    }

    public Venda buscarPorId(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Venda não encontrada"));
    }

    public List<Venda> listarTodas() {
        return repository.findAll();
    }

    public List<Venda> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return repository.findByDataVendaBetween(inicio, fim);
    }

    public List<Venda> buscarPorCliente(Long clienteId) {
        return repository.findByClienteId(clienteId);
    }

    public List<Venda> buscarPorFuncionario(Long funcionarioId) {
        return repository.findByFuncionarioId(funcionarioId);
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Venda não encontrada");
        }
        repository.deleteById(id);
    }
} 