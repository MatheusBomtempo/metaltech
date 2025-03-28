package com.metaltech.repository;

import com.metaltech.model.Funcionario;
import com.metaltech.model.Cargo;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FuncionarioRepository extends PessoaRepository<Funcionario> {
    List<Funcionario> findByCargo(Cargo cargo);
} 