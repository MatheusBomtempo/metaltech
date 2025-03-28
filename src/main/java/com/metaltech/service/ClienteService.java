package com.metaltech.service;

import com.metaltech.model.Cliente;
import com.metaltech.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService extends PessoaService<Cliente, ClienteRepository> {
    
    @Override
    @Transactional
    public Cliente salvar(Cliente cliente) {
        return super.salvar(cliente);
    }
} 