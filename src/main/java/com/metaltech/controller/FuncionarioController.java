package com.metaltech.controller;

import com.metaltech.model.Funcionario;
import com.metaltech.model.Cargo;
import com.metaltech.service.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {
    
    private static final Logger logger = LoggerFactory.getLogger(FuncionarioController.class);
    
    @Autowired
    private FuncionarioService service;

    @PostMapping
    public ResponseEntity<Funcionario> criar(@RequestBody Funcionario funcionario) {
        logger.info("Recebendo requisição para criar funcionário: {}", funcionario);
        try {
            Funcionario funcionarioSalvo = service.salvar(funcionario);
            logger.info("Funcionário criado com sucesso: {}", funcionarioSalvo);
            return ResponseEntity.ok(funcionarioSalvo);
        } catch (Exception e) {
            logger.error("Erro ao criar funcionário: ", e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> buscarPorId(@PathVariable Long id) {
        logger.info("Recebendo requisição para buscar funcionário ID: {}", id);
        try {
            Funcionario funcionario = service.buscarPorId(id);
            return ResponseEntity.ok(funcionario);
        } catch (Exception e) {
            logger.error("Erro ao buscar funcionário: ", e);
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<List<Funcionario>> listarTodos() {
        logger.info("Recebendo requisição para listar todos os funcionários");
        try {
            List<Funcionario> funcionarios = service.listarTodos();
            return ResponseEntity.ok(funcionarios);
        } catch (Exception e) {
            logger.error("Erro ao listar funcionários: ", e);
            throw e;
        }
    }

    @GetMapping("/cargo/{cargo}")
    public ResponseEntity<List<Funcionario>> buscarPorCargo(@PathVariable Cargo cargo) {
        return ResponseEntity.ok(service.buscarPorCargo(cargo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Funcionario> atualizar(@PathVariable Long id, @RequestBody Funcionario funcionario) {
        logger.info("Recebendo requisição para atualizar funcionário ID: {} com dados: {}", id, funcionario);
        try {
            funcionario.setId(id);
            Funcionario funcionarioAtualizado = service.salvar(funcionario);
            return ResponseEntity.ok(funcionarioAtualizado);
        } catch (Exception e) {
            logger.error("Erro ao atualizar funcionário: ", e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        logger.info("Recebendo requisição para deletar funcionário ID: {}", id);
        try {
            service.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Erro ao deletar funcionário: ", e);
            throw e;
        }
    }
} 