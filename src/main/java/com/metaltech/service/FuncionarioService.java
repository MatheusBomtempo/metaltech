package com.metaltech.service;

import com.metaltech.model.Funcionario;
import com.metaltech.model.Cargo;
import com.metaltech.repository.FuncionarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class FuncionarioService extends PessoaService<Funcionario, FuncionarioRepository> {
    
    @Override
    @Transactional
    public Funcionario salvar(Funcionario funcionario) {
        return super.salvar(funcionario);
    }

    public List<Funcionario> listarTodos() {
        return repository.findAll();
    }

    public List<Funcionario> buscarPorCargo(Cargo cargo) {
        return repository.findByCargo(cargo);
    }

    @Transactional
    public Funcionario atualizar(Funcionario funcionario) {
        Funcionario funcionarioExistente = buscarPorId(funcionario.getId());
        
        // Verifica se o documento fiscal está sendo alterado e se já existe
        if (!funcionarioExistente.getDocumentoFiscal().equals(funcionario.getDocumentoFiscal()) && 
            repository.existsByDocumentoFiscal(funcionario.getDocumentoFiscal())) {
            throw new IllegalArgumentException("Já existe uma pessoa cadastrada com este documento fiscal");
        }

        funcionarioExistente.setNome(funcionario.getNome());
        funcionarioExistente.setDocumentoFiscal(funcionario.getDocumentoFiscal());
        funcionarioExistente.setTelefone(funcionario.getTelefone());
        funcionarioExistente.setCargo(funcionario.getCargo());
        funcionarioExistente.setSalario(funcionario.getSalario());
        funcionarioExistente.setDataContratacao(funcionario.getDataContratacao());

        return repository.save(funcionarioExistente);
    }
} 