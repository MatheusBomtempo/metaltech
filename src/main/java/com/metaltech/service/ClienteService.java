package com.metaltech.service;

import com.metaltech.model.Cliente;
import com.metaltech.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ClienteService extends PessoaService<Cliente, ClienteRepository> {
    
    @Override
    @Transactional
    public Cliente salvar(Cliente cliente) {
        return super.salvar(cliente);
    }

    public List<Cliente> listarTodos() {
        return repository.findAll();
    }

    @Transactional
    public Cliente atualizar(Cliente cliente) {
        Cliente clienteExistente = buscarPorId(cliente.getId());
        
        // Verifica se o documento fiscal está sendo alterado e se já existe
        if (!clienteExistente.getDocumentoFiscal().equals(cliente.getDocumentoFiscal()) && 
            repository.existsByDocumentoFiscal(cliente.getDocumentoFiscal())) {
            throw new IllegalArgumentException("Já existe uma pessoa cadastrada com este documento fiscal");
        }

        clienteExistente.setNome(cliente.getNome());
        clienteExistente.setDocumentoFiscal(cliente.getDocumentoFiscal());
        clienteExistente.setTelefone(cliente.getTelefone());
        clienteExistente.setEndereco(cliente.getEndereco());

        return repository.save(clienteExistente);
    }
} 