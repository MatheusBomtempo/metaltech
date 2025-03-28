package com.metaltech.repository;

import com.metaltech.model.Cliente;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends PessoaRepository<Cliente> {
} 