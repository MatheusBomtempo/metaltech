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
    public Fornecedor atualizar(Fornecedor fornecedor) {
        Fornecedor fornecedorExistente = buscarPorId(fornecedor.getId());
        
        // Verifica se o CNPJ está sendo alterado e se já existe
        if (!fornecedorExistente.getCnpj().equals(fornecedor.getCnpj()) && 
            repository.existsByCnpj(fornecedor.getCnpj())) {
            throw new IllegalArgumentException("Já existe um fornecedor cadastrado com este CNPJ");
        }

        fornecedorExistente.setNomeEmpresa(fornecedor.getNomeEmpresa());
        fornecedorExistente.setCnpj(fornecedor.getCnpj());
        fornecedorExistente.setTelefone(fornecedor.getTelefone());
        fornecedorExistente.setTiposMaterial(fornecedor.getTiposMaterial());

        return repository.save(fornecedorExistente);
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Fornecedor não encontrado");
        }
        repository.deleteById(id);
    }
} 