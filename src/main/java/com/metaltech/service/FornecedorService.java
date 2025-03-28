package com.metaltech.service;

import com.metaltech.model.Fornecedor;
import com.metaltech.repository.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class FornecedorService {
    
    @Autowired
    private FornecedorRepository repository;

    @Transactional
    public Fornecedor salvar(Fornecedor fornecedor) {
        if (repository.existsByCnpj(fornecedor.getCnpj())) {
            throw new IllegalArgumentException("Já existe um fornecedor cadastrado com este CNPJ");
        }
        return repository.save(fornecedor);
    }

    public Fornecedor buscarPorId(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Fornecedor não encontrado"));
    }

    public List<Fornecedor> listarTodos() {
        return repository.findAll();
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Fornecedor não encontrado");
        }
        repository.deleteById(id);
    }
} 