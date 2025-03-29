package com.metaltech.service;

import com.metaltech.model.Funcionario;
import com.metaltech.model.Cargo;
import com.metaltech.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Service
public class FuncionarioService {
    
    private static final Logger logger = LoggerFactory.getLogger(FuncionarioService.class);
    
    @Autowired
    private FuncionarioRepository repository;

    @Transactional
    public Funcionario salvar(Funcionario funcionario) {
        try {
            logger.info("Iniciando salvamento do funcionário: {}", funcionario);
            
            // Validar dados obrigatórios
            if (funcionario.getNome() == null || funcionario.getNome().trim().isEmpty()) {
                logger.error("Nome do funcionário é obrigatório");
                throw new IllegalArgumentException("Nome do funcionário é obrigatório");
            }

            if (funcionario.getDocumentoFiscal() == null || funcionario.getDocumentoFiscal().trim().isEmpty()) {
                logger.error("Documento fiscal do funcionário é obrigatório");
                throw new IllegalArgumentException("Documento fiscal do funcionário é obrigatório");
            }

            if (funcionario.getCargo() == null) {
                logger.error("Cargo do funcionário é obrigatório");
                throw new IllegalArgumentException("Cargo do funcionário é obrigatório");
            }

            if (funcionario.getSalario() == null || funcionario.getSalario().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                logger.error("Salário do funcionário deve ser maior que zero");
                throw new IllegalArgumentException("Salário do funcionário deve ser maior que zero");
            }

            // Verificar se já existe um funcionário com o mesmo documento fiscal
            if (repository.existsByDocumentoFiscal(funcionario.getDocumentoFiscal())) {
                logger.error("Já existe um funcionário com o documento fiscal: {}", funcionario.getDocumentoFiscal());
                throw new IllegalArgumentException("Já existe um funcionário com este documento fiscal");
            }

            Funcionario funcionarioSalvo = repository.save(funcionario);
            logger.info("Funcionário salvo com sucesso: {}", funcionarioSalvo);
            return funcionarioSalvo;
        } catch (Exception e) {
            logger.error("Erro ao salvar funcionário: ", e);
            throw e;
        }
    }

    public Funcionario buscarPorId(Long id) {
        try {
            logger.info("Buscando funcionário por ID: {}", id);
            Funcionario funcionario = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado"));
            logger.info("Funcionário encontrado: {}", funcionario);
            return funcionario;
        } catch (Exception e) {
            logger.error("Erro ao buscar funcionário por ID: ", e);
            throw e;
        }
    }

    public List<Funcionario> listarTodos() {
        try {
            logger.info("Listando todos os funcionários");
            List<Funcionario> funcionarios = repository.findAll();
            logger.info("Total de funcionários encontrados: {}", funcionarios.size());
            return funcionarios;
        } catch (Exception e) {
            logger.error("Erro ao listar funcionários: ", e);
            throw e;
        }
    }

    @Transactional
    public void deletar(Long id) {
        try {
            logger.info("Iniciando exclusão do funcionário ID: {}", id);
            if (!repository.existsById(id)) {
                logger.error("Funcionário não encontrado para exclusão: {}", id);
                throw new IllegalArgumentException("Funcionário não encontrado");
            }
            repository.deleteById(id);
            logger.info("Funcionário excluído com sucesso: {}", id);
        } catch (Exception e) {
            logger.error("Erro ao excluir funcionário: ", e);
            throw e;
        }
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