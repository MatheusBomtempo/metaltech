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

    public List<Funcionario> buscarPorCargo(Cargo cargo) {
        return repository.findByCargo(cargo);
    }
} 