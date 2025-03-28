package com.metaltech.service;

import com.metaltech.model.Pessoa;
import com.metaltech.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public abstract class PessoaService<T extends Pessoa, R extends PessoaRepository<T>> {
    
    @Autowired
    protected R repository;

    @Transactional
    public T salvar(T pessoa) {
        if (repository.existsByDocumentoFiscal(pessoa.getDocumentoFiscal())) {
            throw new IllegalArgumentException("Já existe uma pessoa cadastrada com este documento fiscal");
        }
        return repository.save(pessoa);
    }

    public T buscarPorId(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada"));
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Pessoa não encontrada");
        }
        repository.deleteById(id);
    }
} 