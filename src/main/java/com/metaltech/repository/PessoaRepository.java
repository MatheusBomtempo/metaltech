package com.metaltech.repository;

import com.metaltech.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface PessoaRepository<T extends Pessoa> extends JpaRepository<T, Long> {
    boolean existsByDocumentoFiscal(String documentoFiscal);
} 